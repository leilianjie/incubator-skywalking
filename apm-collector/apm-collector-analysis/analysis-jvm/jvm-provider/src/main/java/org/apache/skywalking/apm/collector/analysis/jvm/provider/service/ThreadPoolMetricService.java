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
import org.apache.skywalking.apm.collector.analysis.jvm.define.service.IThreadPoolMetricService;
import org.apache.skywalking.apm.collector.core.graph.Graph;
import org.apache.skywalking.apm.collector.core.graph.GraphManager;
import org.apache.skywalking.apm.collector.core.util.BooleanUtils;
import org.apache.skywalking.apm.collector.core.util.Const;
import org.apache.skywalking.apm.collector.storage.table.jvm.MemoryMetric;
import org.apache.skywalking.apm.collector.storage.table.jvm.ThreadPoolMetric;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author peng-yongsheng
 */
public class ThreadPoolMetricService implements IThreadPoolMetricService {

    private final Logger logger = LoggerFactory.getLogger(ThreadPoolMetricService.class);

    private Graph<ThreadPoolMetric> threadPoolMetricGraph;

    private Graph<ThreadPoolMetric> getThreadPoolMetricGraph() {
        if (isNull(threadPoolMetricGraph)) {
            this.threadPoolMetricGraph = GraphManager.INSTANCE.findGraph(GraphIdDefine.THREAD_POOL_METRIC_PERSISTENCE_GRAPH_ID, ThreadPoolMetric.class);
        }
        return threadPoolMetricGraph;
    }

    @Override
    public void send(String poolName, int instanceId, long timeBucket, long current, long max, long busy) {
        String metricId = String.valueOf(instanceId);
        String id = timeBucket + Const.ID_SPLIT + metricId;

        ThreadPoolMetric threadPoolMetric = new ThreadPoolMetric();
        threadPoolMetric.setId(id);
        threadPoolMetric.setMetricId(metricId);
        threadPoolMetric.setPoolName(poolName);
        threadPoolMetric.setInstanceId(instanceId);
        threadPoolMetric.setCurrent(current);
        threadPoolMetric.setMax(max);
        threadPoolMetric.setBusy(busy);
        threadPoolMetric.setTimes(1L);
        threadPoolMetric.setTimeBucket(timeBucket);

        logger.debug("push to memory metric graph, id: {}", threadPoolMetric.getId());
        getThreadPoolMetricGraph().start(threadPoolMetric);
    }
}
