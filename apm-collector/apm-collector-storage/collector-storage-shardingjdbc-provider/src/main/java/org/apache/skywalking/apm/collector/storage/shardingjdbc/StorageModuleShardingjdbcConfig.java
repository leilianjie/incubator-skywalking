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

package org.apache.skywalking.apm.collector.storage.shardingjdbc;

import org.apache.skywalking.apm.collector.client.shardingjdbc.ShardingjdbcClientConfig;

/**
 * @author linjiaqi
 */
class StorageModuleShardingjdbcConfig extends ShardingjdbcClientConfig {
	private int traceDataTTL = 90;
	private int minuteMetricDataTTL = 90;
	private int hourMetricDataTTL = 36;
	private int dayMetricDataTTL = 45;
	private int monthMetricDataTTL = 18;

	public int getTraceDataTTL() {
		return traceDataTTL;
	}

	public void setTraceDataTTL(int traceDataTTL) {
		this.traceDataTTL = traceDataTTL;
	}

	public int getMinuteMetricDataTTL() {
		return minuteMetricDataTTL;
	}

	public void setMinuteMetricDataTTL(int minuteMetricDataTTL) {
		this.minuteMetricDataTTL = minuteMetricDataTTL;
	}

	public int getHourMetricDataTTL() {
		return hourMetricDataTTL;
	}

	public void setHourMetricDataTTL(int hourMetricDataTTL) {
		this.hourMetricDataTTL = hourMetricDataTTL;
	}

	public int getDayMetricDataTTL() {
		return dayMetricDataTTL;
	}

	public void setDayMetricDataTTL(int dayMetricDataTTL) {
		this.dayMetricDataTTL = dayMetricDataTTL;
	}

	public int getMonthMetricDataTTL() {
		return monthMetricDataTTL;
	}

	public void setMonthMetricDataTTL(int monthMetricDataTTL) {
		this.monthMetricDataTTL = monthMetricDataTTL;
	}

}
