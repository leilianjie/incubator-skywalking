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

package org.apache.skywalking.apm.collector.storage.ui.server;

import java.util.List;

/**
 * @author peng-yongsheng
 */
public class ThreadPoolTrend {
	private List<String> pools;
    private List<Integer> current;
    private List<Integer> max;
    private List<Integer> busy;
    
	public List<String> getPools() {
		return pools;
	}
	public void setPools(List<String> pools) {
		this.pools = pools;
	}
	public List<Integer> getCurrent() {
		return current;
	}
	public void setCurrent(List<Integer> current) {
		this.current = current;
	}
	public List<Integer> getMax() {
		return max;
	}
	public void setMax(List<Integer> max) {
		this.max = max;
	}
	public List<Integer> getBusy() {
		return busy;
	}
	public void setBusy(List<Integer> busy) {
		this.busy = busy;
	}

}
