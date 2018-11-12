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

package org.apache.skywalking.apm.collector.storage.table.jvm;

import org.apache.skywalking.apm.collector.core.data.StreamData;
import org.apache.skywalking.apm.collector.core.data.column.*;
import org.apache.skywalking.apm.collector.core.data.operator.*;

/**
 * @author peng-yongsheng
 */
public class ThreadPoolMetric extends StreamData {

    private static final StringColumn[] STRING_COLUMNS = {
        new StringColumn(ThreadPoolMetricTable.ID, new NonMergeOperation()),
        new StringColumn(ThreadPoolMetricTable.METRIC_ID, new NonMergeOperation()),
        new StringColumn(ThreadPoolMetricTable.POOL_NAME, new NonMergeOperation()),
    };

    private static final LongColumn[] LONG_COLUMNS = {
        new LongColumn(ThreadPoolMetricTable.CURRENT, new MaxMergeOperation()),
        new LongColumn(ThreadPoolMetricTable.MAX, new MaxMergeOperation()),
        new LongColumn(ThreadPoolMetricTable.BUSY, new MaxMergeOperation()),
        new LongColumn(ThreadPoolMetricTable.TIMES, new AddMergeOperation()),
        new LongColumn(ThreadPoolMetricTable.TIME_BUCKET, new NonMergeOperation()),
    };

    private static final IntegerColumn[] INTEGER_COLUMNS = {
        new IntegerColumn(ThreadPoolMetricTable.INSTANCE_ID, new NonMergeOperation()),
    };

    private static final DoubleColumn[] DOUBLE_COLUMNS = {
    };

    public ThreadPoolMetric() {
        super(STRING_COLUMNS, LONG_COLUMNS, INTEGER_COLUMNS, DOUBLE_COLUMNS);
    }

    @Override public String getId() {
        return getDataString(0);
    }

    @Override public void setId(String id) {
        setDataString(0, id);
    }

    @Override public String getMetricId() {
        return getDataString(1);
    }

    @Override public void setMetricId(String metricId) {
        setDataString(1, metricId);
    }

    public String getPoolName(){
    	return getDataString(2);
    }
    
    public void setPoolName(String poolName){
    	setDataString(2, poolName);
    }
    
    public Long getCurrent() {
        return getDataLong(0);
    }

    public void setCurrent(Long current) {
        setDataLong(0, current);
    }

    public Long getMax() {
        return getDataLong(1);
    }

    public void setMax(Long max) {
        setDataLong(1, max);
    }

    public Long getBusy() {
        return getDataLong(2);
    }

    public void setBusy(Long busy) {
        setDataLong(2, busy);
    }

    public Long getTimes() {
        return getDataLong(3);
    }

    public void setTimes(Long times) {
        setDataLong(3, times);
    }

    public Long getTimeBucket() {
        return getDataLong(4);
    }

    public void setTimeBucket(Long timeBucket) {
        setDataLong(4, timeBucket);
    }

    public Integer getInstanceId() {
        return getDataInteger(0);
    }

    public void setInstanceId(Integer instanceId) {
        setDataInteger(0, instanceId);
    }

}
