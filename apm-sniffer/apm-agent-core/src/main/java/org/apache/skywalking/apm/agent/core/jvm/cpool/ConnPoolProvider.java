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


package org.apache.skywalking.apm.agent.core.jvm.cpool;

import java.lang.management.ManagementFactory;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.management.MBeanServer;
import javax.management.ObjectName;

import org.apache.skywalking.apm.agent.core.logging.api.ILog;
import org.apache.skywalking.apm.agent.core.logging.api.LogManager;
import org.apache.skywalking.apm.network.proto.ConnPool;


public enum ConnPoolProvider {
    INSTANCE;
    ConnPoolProvider() {
    }

    public List<ConnPool> getConnPoolMetricList() {
        List<ConnPool> connPoolList = new LinkedList<ConnPool>();
        try {
        	MBeanServer m = ManagementFactory.getPlatformMBeanServer();
        	ObjectName threadObjName = new ObjectName("Catalina:type=DataSource,host=*,context=*,class=javax.sql.DataSource,name=\"*\"");
	        Set<ObjectName> smbi = m.queryNames(threadObjName, null);
	    	for (ObjectName obj : smbi) {
	    		ObjectName objname = new ObjectName(obj.getCanonicalName());
	    		ConnPool.Builder poolBuilder = ConnPool.newBuilder();
	    		poolBuilder.setPoolName(obj.getKeyProperty("name"));
	    		poolBuilder.setActive(Long.valueOf(String.valueOf(m.getAttribute(objname, "numActive"))));
	    		poolBuilder.setMax(Long.valueOf(String.valueOf(m.getAttribute(objname, "maxTotal"))));
	    		connPoolList.add(poolBuilder.build());
	    	}
	    	
	    	ObjectName ncObjName = new ObjectName("uap.middleware:type=connection pool,name=fc*");
	    	Set<ObjectName> ncos = m.queryNames(ncObjName, null);
	    	for (ObjectName obj : ncos) {
	    		ObjectName objname = new ObjectName(obj.getCanonicalName());
	    		ConnPool.Builder poolBuilder = ConnPool.newBuilder();
	    		poolBuilder.setPoolName(obj.getKeyProperty("name"));
	    		poolBuilder.setActive(Long.valueOf(String.valueOf(m.getAttribute(objname, "InUsedConnection"))));
	    		poolBuilder.setMax(Long.valueOf(String.valueOf(m.getAttribute(objname, "MaxConnection"))));
	    		connPoolList.add(poolBuilder.build());
	    	}
        } catch (Exception e) {
        	ILog logger = LogManager.getLogger(ConnPoolProvider.class);
	        logger.error(e, "Cant not get DataSource MBean Attribute ......");
        }
        return connPoolList;
    }

}
