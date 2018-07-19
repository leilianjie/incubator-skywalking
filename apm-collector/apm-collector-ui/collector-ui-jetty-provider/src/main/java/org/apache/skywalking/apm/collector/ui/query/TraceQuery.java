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

package org.apache.skywalking.apm.collector.ui.query;

import org.apache.skywalking.apm.collector.core.UnexpectedException;
import org.apache.skywalking.apm.collector.core.module.ModuleManager;
import org.apache.skywalking.apm.collector.core.util.*;
import org.apache.skywalking.apm.collector.storage.ui.trace.*;
import org.apache.skywalking.apm.collector.ui.graphql.Query;
import org.apache.skywalking.apm.collector.ui.service.*;
import org.apache.skywalking.apm.collector.ui.utils.*;

import static java.util.Objects.*;

/**
 * @author peng-yongsheng
 */
public class TraceQuery implements Query {

    private final ModuleManager moduleManager;
    private SegmentTopService segmentTopService;
    private TraceStackService traceStackService;

    public TraceQuery(ModuleManager moduleManager) {
        this.moduleManager = moduleManager;
    }

    private SegmentTopService getSegmentTopService() {
        if (isNull(segmentTopService)) {
            this.segmentTopService = new SegmentTopService(moduleManager);
        }
        return segmentTopService;
    }

    private TraceStackService getTraceStackService() {
        if (isNull(traceStackService)) {
            this.traceStackService = new TraceStackService(moduleManager);
        }
        return traceStackService;
    }

    public TraceBrief queryBasicTraces(TraceQueryCondition condition) {
        long startSecondTimeBucket = 0;
        long endSecondTimeBucket = 0;
        String traceId = Const.EMPTY_STRING;

        if (StringUtils.isNotEmpty(condition.getTraceId())) {
            traceId = condition.getTraceId();
        } else if (nonNull(condition.getQueryDuration())) {
            startSecondTimeBucket = DurationUtils.INSTANCE.startTimeDurationToSecondTimeBucket(condition.getQueryDuration().getStep(), condition.getQueryDuration().getStart());
            endSecondTimeBucket = DurationUtils.INSTANCE.endTimeDurationToSecondTimeBucket(condition.getQueryDuration().getStep(), condition.getQueryDuration().getEnd());
        } else {
            throw new UnexpectedException("The condition must contains either queryDuration or traceId.");
        }

        long minDuration = condition.getMinTraceDuration();
        long maxDuration = condition.getMaxTraceDuration();
        String operationName = condition.getOperationName();
        String userId = condition.getUserId();
        int applicationId = condition.getApplicationId();
        TraceState traceState = condition.getTraceState();
        QueryOrder queryOrder = condition.getQueryOrder();

        PaginationUtils.Page page = PaginationUtils.INSTANCE.exchange(condition.getPaging());
        return getSegmentTopService().loadTop(startSecondTimeBucket, endSecondTimeBucket, minDuration, maxDuration, operationName, userId, traceId, applicationId, page.getLimit(), page.getFrom(), traceState, queryOrder);
    }

    public Trace queryTrace(String traceId) {
        return getTraceStackService().load(traceId);
    }
}
