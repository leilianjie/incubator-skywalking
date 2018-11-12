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

package org.apache.skywalking.apm.collector.storage.es.dao.tpool;

import java.io.IOException;
import java.util.Map;

import org.apache.skywalking.apm.collector.client.elasticsearch.ElasticSearchClient;
import org.apache.skywalking.apm.collector.core.annotations.trace.GraphComputingMetric;
import org.apache.skywalking.apm.collector.storage.es.base.dao.AbstractPersistenceEsDAO;
import org.apache.skywalking.apm.collector.storage.table.jvm.ThreadPoolMetric;
import org.apache.skywalking.apm.collector.storage.table.jvm.ThreadPoolMetricTable;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;

/**
 * @author peng-yongsheng
 */
public abstract class AbstractThreadPoolMetricEsPersistenceDAO extends AbstractPersistenceEsDAO<ThreadPoolMetric> {

    AbstractThreadPoolMetricEsPersistenceDAO(ElasticSearchClient client) {
        super(client);
    }

    @Override
    protected final String timeBucketColumnNameForDelete() {
        return ThreadPoolMetricTable.TIME_BUCKET.getName();
    }

    @Override
    protected final ThreadPoolMetric esDataToStreamData(Map<String, Object> source) {
    	ThreadPoolMetric threadPoolMetric = new ThreadPoolMetric();
    	threadPoolMetric.setMetricId((String)source.get(ThreadPoolMetricTable.METRIC_ID.getName()));
    	threadPoolMetric.setPoolName((String)source.get(ThreadPoolMetricTable.POOL_NAME.getName()));
    	threadPoolMetric.setInstanceId(((Number)source.get(ThreadPoolMetricTable.INSTANCE_ID.getName())).intValue());

    	threadPoolMetric.setCurrent(((Number)source.get(ThreadPoolMetricTable.CURRENT.getName())).longValue());
    	threadPoolMetric.setMax(((Number)source.get(ThreadPoolMetricTable.MAX.getName())).longValue());
    	threadPoolMetric.setBusy(((Number)source.get(ThreadPoolMetricTable.BUSY.getName())).longValue());
        threadPoolMetric.setTimes(((Number)source.get(ThreadPoolMetricTable.TIMES.getName())).longValue());

        threadPoolMetric.setTimeBucket(((Number)source.get(ThreadPoolMetricTable.TIME_BUCKET.getName())).longValue());
        return threadPoolMetric;
    }

    @Override
    protected final XContentBuilder esStreamDataToEsData(ThreadPoolMetric streamData) throws IOException {
        return XContentFactory.jsonBuilder().startObject()
            .field(ThreadPoolMetricTable.METRIC_ID.getName(), streamData.getMetricId())
            .field(ThreadPoolMetricTable.POOL_NAME.getName(), streamData.getPoolName())
            .field(ThreadPoolMetricTable.INSTANCE_ID.getName(), streamData.getInstanceId())
            .field(ThreadPoolMetricTable.CURRENT.getName(), streamData.getCurrent())
            .field(ThreadPoolMetricTable.MAX.getName(), streamData.getMax())
            .field(ThreadPoolMetricTable.BUSY.getName(), streamData.getBusy())
            .field(ThreadPoolMetricTable.TIMES.getName(), streamData.getTimes())
            .field(ThreadPoolMetricTable.TIME_BUCKET.getName(), streamData.getTimeBucket())
            .endObject();
    }

    @GraphComputingMetric(name = "/persistence/get/" + ThreadPoolMetricTable.TABLE)
    @Override public final ThreadPoolMetric get(String id) {
        return super.get(id);
    }
}
