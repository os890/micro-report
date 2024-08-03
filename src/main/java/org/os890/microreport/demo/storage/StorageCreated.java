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

import one.microstream.storage.types.StorageManager;

public class StorageCreated {
    private final MicroStore microStore;
    private final StorageManager storageManager;
    private final boolean createSampleData;

    public StorageCreated(MicroStore microStore, StorageManager storageManager, boolean createSampleData) {
        this.microStore = microStore;
        this.storageManager = storageManager;
        this.createSampleData = createSampleData;
    }

    public MicroStore getMicroStore() {
        return microStore;
    }

    public StorageManager getStorageManager() {
        return storageManager;
    }

    public boolean isCreateSampleData() {
        return createSampleData;
    }
}
