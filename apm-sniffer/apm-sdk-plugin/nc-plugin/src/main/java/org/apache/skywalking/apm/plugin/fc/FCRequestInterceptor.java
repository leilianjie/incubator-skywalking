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


package org.apache.skywalking.apm.plugin.fc;

import java.lang.reflect.Method;

import org.apache.skywalking.apm.agent.core.context.CarrierItem;
import org.apache.skywalking.apm.agent.core.context.ContextCarrier;
import org.apache.skywalking.apm.agent.core.context.ContextManager;
import org.apache.skywalking.apm.agent.core.context.tag.Tags;
import org.apache.skywalking.apm.agent.core.context.trace.AbstractSpan;
import org.apache.skywalking.apm.agent.core.context.trace.SpanLayer;
import org.apache.skywalking.apm.agent.core.logging.api.ILog;
import org.apache.skywalking.apm.agent.core.logging.api.LogManager;
import org.apache.skywalking.apm.agent.core.plugin.interceptor.enhance.EnhancedInstance;
import org.apache.skywalking.apm.agent.core.plugin.interceptor.enhance.InstanceMethodsAroundInterceptor;
import org.apache.skywalking.apm.agent.core.plugin.interceptor.enhance.MethodInterceptResult;
import org.apache.skywalking.apm.network.trace.component.ComponentsDefine;

import nc.bs.framework.common.InvocationInfo;
import nc.bs.framework.rmi.Address;
import nc.bs.framework.rmi.RemoteChannel;

/**
 * {@link DubboInterceptor} define how to enhance class {@link com.alibaba.dubbo.monitor.support.MonitorFilter#invoke(Invoker,
 * Invocation)}. the trace context transport to the provider side by {@link RpcContext#attachments}.but all the version
 * of dubbo framework below 2.8.3 don't support {@link RpcContext#attachments}, we support another way to support it.
 *
 * @author zhangxin
 */
public class FCRequestInterceptor implements InstanceMethodsAroundInterceptor {
	  private static final ILog logger = LogManager.getLogger(FCRequestInterceptor.class);

    /**
     * <h2>Consumer:</h2> The serialized trace context data will
     * inject to the {@link RpcContext#attachments} for transport to provider side.
     * <p>
     * <h2>Provider:</h2> The serialized trace context data will extract from
     * {@link RpcContext#attachments}. current trace segment will ref if the serialize context data is not null.
     */
    @Override
    public void beforeMethod(EnhancedInstance objInst, Method method, Object[] allArguments,
        Class<?>[] argumentsTypes, MethodInterceptResult result) throws Throwable {
    	logger.error("fc request****************************");
    	Address  target = (Address )allArguments[0];
    	InvocationInfo  invocation = (InvocationInfo )allArguments[1];
    	RemoteChannel  rc = (RemoteChannel )allArguments[2];
    	//invocation.setAddress(target);
    	logger.error("fc request****************:"+target.getHost());
        AbstractSpan span;
        final String host = target.getHost();
        final String path = target.getPath();
            final ContextCarrier contextCarrier = new ContextCarrier();
            //nc.bs.framework.common.NCLocator/lookup
            span = ContextManager.createExitSpan(generateOperationName(target, invocation), contextCarrier, host + ":" + path);
            //invocation.getAttachments().put("contextData", contextDataStr);
            //@see https://github.com/alibaba/dubbo/blob/dubbo-2.5.3/dubbo-rpc/dubbo-rpc-api/src/main/java/com/alibaba/dubbo/rpc/RpcInvocation.java#L154-L161
            CarrierItem next = contextCarrier.items();
            while (next.hasNext()) {
                next = next.next();
            	logger.error("fc-request-sky:****************key:"+next.getHeadKey()+"value:"+next.getHeadValue());
                rc.setRequestHeader(next.getHeadKey(), next.getHeadValue());
                //rpcContext.getAttachments().put(next.getHeadKey(), next.getHeadValue());
            }
                //Tags.URL.set(span, generateRequestURL(requestURL, invocation));
        //span.setComponent(ComponentsDefine.DUBBO);
        Tags.URL.set(span, generateRequestURL(target, invocation));
        span.setComponent(ComponentsDefine.FC_REQUEST);
        SpanLayer.asHttp(span);
  
    }

    @Override
    public Object afterMethod(EnhancedInstance objInst, Method method, Object[] allArguments,
        Class<?>[] argumentsTypes, Object ret) throws Throwable {
       // Result result = (Result)ret;
        //if (result != null && result.getException() != null) {
        //    dealException(result.getException());
       // }

        ContextManager.stopSpan();
        return ret;
    }

    @Override
    public void handleMethodException(EnhancedInstance objInst, Method method, Object[] allArguments,
        Class<?>[] argumentsTypes, Throwable t) {
        dealException(t);
    }

    /**
     * Log the throwable, which occurs in Dubbo RPC service.
     */
    private void dealException(Throwable throwable) {
        AbstractSpan span = ContextManager.activeSpan();
        span.errorOccurred();
        span.log(throwable);
    }

    /**
     * Format operation name. e.g. org.apache.skywalking.apm.plugin.test.Test.test(String)
     *
     * @return operation name.
     */
    private String generateOperationName(Address address, InvocationInfo invocation) {
        StringBuilder operationName = new StringBuilder();
        operationName.append(address.getPath());
        operationName.append("." + invocation.getMethodName() + "(");
        for (Class<?> classes : invocation.getParametertypes()) {
            operationName.append(classes.getSimpleName() + ",");
        }

        if (invocation.getParametertypes().length > 0) {
            operationName.delete(operationName.length() - 1, operationName.length());
        }

        operationName.append(")");

        return operationName.toString();
    }

    /**
     * Format request url.
     * e.g. dubbo://127.0.0.1:20880/org.apache.skywalking.apm.plugin.test.Test.test(String).
     *
     * @return request url.
     */
    private String generateRequestURL(Address address, InvocationInfo invocation) {
        StringBuilder requestURL = new StringBuilder();
        requestURL.append(address.getProtocol() + "://");
        requestURL.append(address.getHost());
        requestURL.append(":" + address.getPort() + "/");
        requestURL.append(generateOperationName(address, invocation));
        return requestURL.toString();
    }
}
