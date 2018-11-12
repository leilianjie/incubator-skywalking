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

package org.apache.skywalking.apm.collector.storage.es.dao.cpool;

import java.io.IOException;
import java.util.Map;
import org.apache.skywalking.apm.collector.client.elasticsearch.ElasticSearchClient;
import org.apache.skywalking.apm.collector.core.annotations.trace.GraphComputingMetric;
import org.apache.skywalking.apm.collector.storage.es.base.dao.AbstractPersistenceEsDAO;
import org.apache.skywalking.apm.collector.storage.table.jvm.*;
import org.elasticsearch.common.xcontent.*;

/**
 * @author peng-yongsheng
 */
public abstract class AbstractConnPoolMetricEsPersistenceDAO extends AbstractPersistenceEsDAO<ConnPoolMetric> {

    AbstractConnPoolMetricEsPersistenceDAO(ElasticSearchClient client) {
        super(client);
    }

    @Override
    protected final String timeBucketColumnNameForDelete() {
        return ConnPoolMetricTable.TIME_BUCKET.getName();
    }

    @Override
    protected final ConnPoolMetric esDataToStreamData(Map<String, Object> source) {
    	ConnPoolMetric connPoolMetric = new ConnPoolMetric();
    	connPoolMetric.setMetricId((String)source.get(ConnPoolMetricTable.METRIC_ID.getName()));
    	connPoolMetric.setPoolName((String)source.get(ConnPoolMetricTable.POOL_NAME.getName()));
    	connPoolMetric.setInstanceId(((Number)source.get(ConnPoolMetricTable.INSTANCE_ID.getName())).intValue());

    	connPoolMetric.setMax(((Number)source.get(ConnPoolMetricTable.MAX.getName())).longValue());
    	connPoolMetric.setActive(((Number)source.get(ConnPoolMetricTable.ACTIVE.getName())).longValue());
    	connPoolMetric.setTimes(((Number)source.get(ConnPoolMetricTable.TIMES.getName())).longValue());

    	connPoolMetric.setTimeBucket(((Number)source.get(MemoryPoolMetricTable.TIME_BUCKET.getName())).longValue());
        return connPoolMetric;
    }

    @Override
    protected final XContentBuilder esStreamDataToEsData(ConnPoolMetric streamData) throws IOException {
        return XContentFactory.jsonBuilder().startObject()
            .field(ConnPoolMetricTable.METRIC_ID.getName(), streamData.getMetricId())
            .field(ConnPoolMetricTable.POOL_NAME.getName(), streamData.getPoolName())
            .field(ConnPoolMetricTable.INSTANCE_ID.getName(), streamData.getInstanceId())
            .field(ConnPoolMetricTable.MAX.getName(), streamData.getMax())
            .field(ConnPoolMetricTable.ACTIVE.getName(), streamData.getActive())
            .field(ConnPoolMetricTable.TIMES.getName(), streamData.getTimes())
            .field(ConnPoolMetricTable.TIME_BUCKET.getName(), streamData.getTimeBucket())
            .endObject();
    }

    @GraphComputingMetric(name = "/persistence/get/" + ConnPoolMetricTable.TABLE)
    @Override public final ConnPoolMetric get(String id) {
        return super.get(id);
    }
}
