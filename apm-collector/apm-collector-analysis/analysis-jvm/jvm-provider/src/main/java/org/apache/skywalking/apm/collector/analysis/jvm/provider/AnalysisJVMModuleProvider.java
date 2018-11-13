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

package org.apache.skywalking.apm.collector.analysis.jvm.provider;

import org.apache.skywalking.apm.collector.analysis.jvm.define.AnalysisJVMModule;
import org.apache.skywalking.apm.collector.analysis.jvm.define.service.IConnPoolMetricService;
import org.apache.skywalking.apm.collector.analysis.jvm.define.service.ICpuMetricService;
import org.apache.skywalking.apm.collector.analysis.jvm.define.service.IGCMetricService;
import org.apache.skywalking.apm.collector.analysis.jvm.define.service.IMemoryMetricService;
import org.apache.skywalking.apm.collector.analysis.jvm.define.service.IMemoryPoolMetricService;
import org.apache.skywalking.apm.collector.analysis.jvm.define.service.IThreadPoolMetricService;
import org.apache.skywalking.apm.collector.analysis.jvm.provider.service.ConnPoolMetricService;
import org.apache.skywalking.apm.collector.analysis.jvm.provider.service.CpuMetricService;
import org.apache.skywalking.apm.collector.analysis.jvm.provider.service.GCMetricService;
import org.apache.skywalking.apm.collector.analysis.jvm.provider.service.MemoryMetricService;
import org.apache.skywalking.apm.collector.analysis.jvm.provider.service.MemoryPoolMetricService;
import org.apache.skywalking.apm.collector.analysis.jvm.provider.service.ThreadPoolMetricService;
import org.apache.skywalking.apm.collector.analysis.jvm.provider.worker.cpool.ConnPoolMetricPersistenceGraph;
import org.apache.skywalking.apm.collector.analysis.jvm.provider.worker.cpu.CpuMetricPersistenceGraph;
import org.apache.skywalking.apm.collector.analysis.jvm.provider.worker.gc.GCMetricPersistenceGraph;
import org.apache.skywalking.apm.collector.analysis.jvm.provider.worker.memory.MemoryMetricPersistenceGraph;
import org.apache.skywalking.apm.collector.analysis.jvm.provider.worker.memorypool.MemoryPoolMetricPersistenceGraph;
import org.apache.skywalking.apm.collector.analysis.jvm.provider.worker.tpool.ThreadPoolMetricPersistenceGraph;
import org.apache.skywalking.apm.collector.analysis.worker.model.base.WorkerCreateListener;
import org.apache.skywalking.apm.collector.analysis.worker.timer.PersistenceTimer;
import org.apache.skywalking.apm.collector.core.module.ModuleConfig;
import org.apache.skywalking.apm.collector.core.module.ModuleDefine;
import org.apache.skywalking.apm.collector.core.module.ModuleProvider;
import org.apache.skywalking.apm.collector.core.module.ServiceNotProvidedException;
import org.apache.skywalking.apm.collector.remote.RemoteModule;
import org.apache.skywalking.apm.collector.storage.StorageModule;

/**
 * @author peng-yongsheng
 */
public class AnalysisJVMModuleProvider extends ModuleProvider {

    public static final String NAME = "default";
    private final AnalysisJVMModuleConfig config;

    public AnalysisJVMModuleProvider() {
        super();
        this.config = new AnalysisJVMModuleConfig();
    }

    @Override public String name() {
        return NAME;
    }

    @Override public Class<? extends ModuleDefine> module() {
        return AnalysisJVMModule.class;
    }

    @Override public ModuleConfig createConfigBeanIfAbsent() {
        return config;
    }

    @Override public void prepare() throws ServiceNotProvidedException {
        this.registerServiceImplementation(ICpuMetricService.class, new CpuMetricService());
        this.registerServiceImplementation(IGCMetricService.class, new GCMetricService());
        this.registerServiceImplementation(IMemoryMetricService.class, new MemoryMetricService());
        this.registerServiceImplementation(IMemoryPoolMetricService.class, new MemoryPoolMetricService());
        this.registerServiceImplementation(IConnPoolMetricService.class, new ConnPoolMetricService());
        this.registerServiceImplementation(IThreadPoolMetricService.class, new ThreadPoolMetricService());
    }

    @Override public void start() {
        WorkerCreateListener workerCreateListener = new WorkerCreateListener();

        graphCreate(workerCreateListener);

        PersistenceTimer.INSTANCE.start(getManager(), workerCreateListener.getPersistenceWorkers());
    }

    @Override public void notifyAfterCompleted() {
    }

    @Override public String[] requiredModules() {
        return new String[] {StorageModule.NAME, RemoteModule.NAME};
    }

    private void graphCreate(WorkerCreateListener workerCreateListener) {
        new CpuMetricPersistenceGraph(getManager(), workerCreateListener).create();
        new GCMetricPersistenceGraph(getManager(), workerCreateListener).create();
        new MemoryMetricPersistenceGraph(getManager(), workerCreateListener).create();
        new MemoryPoolMetricPersistenceGraph(getManager(), workerCreateListener).create();
        new ThreadPoolMetricPersistenceGraph(getManager(), workerCreateListener).create();
        new ConnPoolMetricPersistenceGraph(getManager(), workerCreateListener).create();
    }
}
