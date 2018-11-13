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


package org.apache.skywalking.apm.agent.core.jvm.tpool;

import java.lang.management.ManagementFactory;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.management.MBeanServer;
import javax.management.ObjectName;

import org.apache.skywalking.apm.agent.core.logging.api.ILog;
import org.apache.skywalking.apm.agent.core.logging.api.LogManager;
import org.apache.skywalking.apm.network.proto.ThreadPool;


public enum ThreadPoolProvider {
    INSTANCE;
    
    ThreadPoolProvider() {
    }

    public List<ThreadPool> getThreadPoolMetricList() {
    	MBeanServer m = ManagementFactory.getPlatformMBeanServer();
    	ObjectName threadObjName = new ObjectName("Catalina:type=ThreadPool,name=\"http*\"");
        List<ThreadPool> threadPoolList = new LinkedList<ThreadPool>();
        try {
	        Set<ObjectName> smbi = m.queryNames(threadObjName, null);
	    	for (ObjectName obj : smbi) {
	    		ObjectName objname = new ObjectName(obj.getCanonicalName());
				ThreadPool.Builder poolBuilder = ThreadPool.newBuilder();
	    		poolBuilder.setPoolName(obj.getKeyProperty("name"));
	    		long current = Long.valueOf(String.valueOf(m.getAttribute(objname, "currentThreadCount")));
	    		poolBuilder.setCurrent(current<0?0:current);
	    		long busy = Long.valueOf(String.valueOf(m.getAttribute(objname, "currentThreadsBusy")));
	    		poolBuilder.setBusy(busy<0?0:busy);
	    		poolBuilder.setMax(Long.valueOf(String.valueOf(m.getAttribute(objname, "maxThreads"))));
	    		threadPoolList.add(poolBuilder.build());
	    	}
        } catch (Exception e) {
        	ILog logger = LogManager.getLogger(ThreadPoolProvider.class);
	        logger.error(e, "Cant not get http Thread pool MBean Attribute ......");
        }
        return threadPoolList;
    }

}
