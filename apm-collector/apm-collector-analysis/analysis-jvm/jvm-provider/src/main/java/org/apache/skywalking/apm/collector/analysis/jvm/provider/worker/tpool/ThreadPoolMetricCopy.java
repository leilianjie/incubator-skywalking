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

package org.apache.skywalking.apm.collector.analysis.jvm.provider.worker.tpool;

import org.apache.skywalking.apm.collector.storage.table.jvm.ThreadPoolMetric;

/**
 * @author peng-yongsheng
 */
public class ThreadPoolMetricCopy {

    public static ThreadPoolMetric copy(ThreadPoolMetric threadPoolMetric) {
        ThreadPoolMetric newThreadPoolMetric = new ThreadPoolMetric();
        newThreadPoolMetric.setId(threadPoolMetric.getId());
        newThreadPoolMetric.setMetricId(threadPoolMetric.getMetricId());
        newThreadPoolMetric.setPoolName(threadPoolMetric.getPoolName());

        newThreadPoolMetric.setInstanceId(threadPoolMetric.getInstanceId());

        newThreadPoolMetric.setCurrent(threadPoolMetric.getCurrent());
        newThreadPoolMetric.setMax(threadPoolMetric.getMax());
        newThreadPoolMetric.setBusy(threadPoolMetric.getBusy());
        newThreadPoolMetric.setTimes(threadPoolMetric.getTimes());

        newThreadPoolMetric.setTimeBucket(threadPoolMetric.getTimeBucket());
        return newThreadPoolMetric;
    }
}
