/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.os890.microreport.demo.storage;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.Priority;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Event;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import one.microstream.persistence.binary.jdk8.types.BinaryHandlersJDK8;
import one.microstream.storage.embedded.configuration.types.EmbeddedStorageConfiguration;
import one.microstream.storage.types.StorageManager;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.util.function.Predicate;

import static java.lang.Integer.MAX_VALUE;
import static java.lang.Integer.MIN_VALUE;

@ApplicationScoped
public class MicroStore {
    protected StorageManager storageManager;

    @Inject
    protected StorageRoot storageRoot;

    @Inject
    private Event<StorageCreated> storageCreatedEvent;

    @Inject
    @ConfigProperty(name = "microreport.createDemoData", defaultValue = "false")
    private Boolean createDemoData;

    @PostConstruct
    protected void initOnCreate() {
        init();
    }

    @Inject
    private StorageManagerAdapter storageManagerAdapter;

    protected void init() {
        this.storageManager = createStorageManager();
        this.storageManagerAdapter.bind(this.storageManager); //see comment at StorageManagerAdapter

        if (storageManager.root() == null) {
            storageManager.setRoot(storageRoot.get());
            storageManager.storeRoot();
            storageCreatedEvent.fire(new StorageCreated(this, storageManager, createDemoData));
        } else {
            storageRoot.apply((DiskState) storageManager.root());
        }
    }

    protected StorageManager createStorageManager() {
        return EmbeddedStorageConfiguration.Builder()
                .setChannelCount(Math.max(1, Integer.highestOneBit(Runtime.getRuntime().availableProcessors() - 1)))
                .setStorageDirectoryInUserHome("microreport-demo/storage")
                .createEmbeddedStorageFoundation()
                .onConnectionFoundation(BinaryHandlersJDK8::registerJDK8TypeHandlers)
                .start();
    }

    public AccountStorage topAccountStorage() {
        return this.storageRoot.getTopAccountStorage();
    }

    public AccountStorage accountStorage() {
        return this.storageRoot.getAccountStorage();
    }

    public long fileCount() {
        return this.storageManager.createStorageStatistics().fileCount();
    }

    public boolean isEmpty() {
        return storageRoot.find(o -> true) == null;
    }
}
