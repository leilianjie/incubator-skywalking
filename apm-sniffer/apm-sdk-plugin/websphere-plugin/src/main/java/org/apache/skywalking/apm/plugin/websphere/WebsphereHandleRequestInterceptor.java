/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */


package org.apache.skywalking.apm.plugin.websphere;

import java.lang.reflect.Method;

import javax.servlet.http.HttpServletRequest;

import org.apache.skywalking.apm.agent.core.context.CarrierItem;
import org.apache.skywalking.apm.agent.core.context.ContextCarrier;
import org.apache.skywalking.apm.agent.core.context.ContextManager;
import org.apache.skywalking.apm.agent.core.context.tag.Tags;
import org.apache.skywalking.apm.agent.core.context.trace.AbstractSpan;
import org.apache.skywalking.apm.agent.core.context.trace.SpanLayer;
import org.apache.skywalking.apm.agent.core.context.trace.TraceSegment;
import org.apache.skywalking.apm.agent.core.plugin.interceptor.enhance.EnhancedInstance;
import org.apache.skywalking.apm.agent.core.plugin.interceptor.enhance.InstanceMethodsAroundInterceptor;
import org.apache.skywalking.apm.agent.core.plugin.interceptor.enhance.MethodInterceptResult;
import org.apache.skywalking.apm.network.trace.component.ComponentsDefine;

import com.ibm.websphere.servlet.request.IRequest;
import com.ibm.ws.webcontainer.channel.WCCResponseImpl;

/**
 * {@link WebsphereHandleRequestInterceptor} fetch the serialized context data by using {@link
 * HttpServletRequest#getHeader(String)}. The {@link TraceSegment#refs} of current trace segment will reference to the
 * trace segment id of the previous level if the serialized context is not null.
 */
public class WebsphereHandleRequestInterceptor implements InstanceMethodsAroundInterceptor {

    /**
     * * The {@link TraceSegment#refs} of current trace segment will reference to the
     * trace segment id of the previous level if the serialized context is not null.
     *
     * @param objInst
     * @param method
     * @param allArguments
     * @param argumentsTypes
     * @param result change this result, if you want to truncate the method.
     * @throws Throwable
     */
    @Override public void beforeMethod(EnhancedInstance objInst, Method method, Object[] allArguments,
        Class<?>[] argumentsTypes, MethodInterceptResult result) throws Throwable {
    	com.ibm.websphere.servlet.request.IRequest request = (com.ibm.websphere.servlet.request.IRequest)allArguments[0];
        ContextCarrier contextCarrier = new ContextCarrier();

        CarrierItem next = contextCarrier.items();
        while (next.hasNext()) {
            next = next.next();
            next.setHeadValue(request.getHeader(next.getHeadKey()));
        }

        AbstractSpan span = ContextManager.createEntrySpan(request.getRequestURI(), contextCarrier);
        Tags.URL.set(span, generateRequestURL(request));
        Tags.HTTP.METHOD.set(span, request.getMethod());
        span.setComponent(ComponentsDefine.WEBSPHERE);
        SpanLayer.asHttp(span);

    }

    private String generateRequestURL(IRequest request) {
    	StringBuilder url = new StringBuilder();
    	url.append(request.getScheme()).append("://")
    	.append(request.getServerName()).append(":").append(request.getServerPort())
    	.append(request.getRequestURI()).append("?").append(request.getQueryString());
		return url.toString();
	}

	@Override public Object afterMethod(EnhancedInstance objInst, Method method, Object[] allArguments,
        Class<?>[] argumentsTypes, Object ret) throws Throwable {
    	int status = 200; 
    	if(allArguments[1] instanceof WCCResponseImpl){
    	   WCCResponseImpl r = (WCCResponseImpl) allArguments[1];
    	   status = r.getHttpResponse().getStatusCodeAsInt();
    	}else if(allArguments[1] instanceof EnhancedInstance){
    		EnhancedInstance r = (EnhancedInstance) allArguments[1];
    		if(r.getSkyWalkingDynamicField() != null){
    			status = (Integer)r.getSkyWalkingDynamicField();
    		}
    	}

        AbstractSpan span = ContextManager.activeSpan();
        if (status >= 400) {
            span.errorOccurred();
            Tags.STATUS_CODE.set(span, Integer.toString(status));
        }
        ContextManager.stopSpan();
        return ret;
    }

    @Override public void handleMethodException(EnhancedInstance objInst, Method method, Object[] allArguments,
        Class<?>[] argumentsTypes, Throwable t) {
        AbstractSpan span = ContextManager.activeSpan();
        span.log(t);
        span.errorOccurred();
    }
}
