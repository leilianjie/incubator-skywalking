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

package org.apache.skywalking.apm.collector.analysis.jvm.provider.service;

import static java.util.Objects.isNull;

import org.apache.skywalking.apm.collector.analysis.jvm.define.graph.GraphIdDefine;
import org.apache.skywalking.apm.collector.analysis.jvm.define.service.IConnPoolMetricService;
import org.apache.skywalking.apm.collector.core.graph.Graph;
import org.apache.skywalking.apm.collector.core.graph.GraphManager;
import org.apache.skywalking.apm.collector.core.util.Const;
import org.apache.skywalking.apm.collector.storage.table.jvm.ConnPoolMetric;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author peng-yongsheng
 */
public class ConnPoolMetricService implements IConnPoolMetricService {

    private final Logger logger = LoggerFactory.getLogger(ConnPoolMetricService.class);

    private Graph<ConnPoolMetric> connPoolMetricGraph;

    private Graph<ConnPoolMetric> getConnPoolMetricGraph() {
        if (isNull(connPoolMetricGraph)) {
            this.connPoolMetricGraph = GraphManager.INSTANCE.findGraph(GraphIdDefine.CONN_POOL_METRIC_PERSISTENCE_GRAPH_ID, ConnPoolMetric.class);
        }
        return connPoolMetricGraph;
    }

    @Override
    public void send(String poolName, int instanceId, long timeBucket, long max, long active) {
        String metricId = String.valueOf(instanceId);
        String id = timeBucket + Const.ID_SPLIT + instanceId;

        ConnPoolMetric connPoolMetric = new ConnPoolMetric();
        connPoolMetric.setId(id);
        connPoolMetric.setMetricId(metricId);
        connPoolMetric.setPoolName(poolName);
        connPoolMetric.setInstanceId(instanceId);
        connPoolMetric.setMax(max);
        connPoolMetric.setActive(active);
        connPoolMetric.setTimes(1L);
        connPoolMetric.setTimeBucket(timeBucket);

        logger.debug("push to memory metric graph, id: {}", connPoolMetric.getId());
        getConnPoolMetricGraph().start(connPoolMetric);
    }
}
