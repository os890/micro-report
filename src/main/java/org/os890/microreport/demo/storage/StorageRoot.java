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

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class StorageRoot {
    //use indirection to avoid issues with cdi-proxies
    private Persisted persistedStorage = new Persisted();

    public AccountStorage getTopAccountStorage() {
        return persistedStorage.topAccountStorage;
    }

    public AccountStorage getAccountStorage() {
        return persistedStorage.accountStorage;
    }

    public DiskState get() {
        DiskState diskState = new DiskState(persistedStorage.topAccountStorage.getAccountList(), persistedStorage.accountStorage.getAccountList());

        return diskState;
    }

    public void apply(DiskState restoredRoot) {
        Persisted persisted = new Persisted();
        persisted.topAccountStorage.getAccountList().addAll(restoredRoot.getTopAccountList());
        persisted.accountStorage.getAccountList().addAll(restoredRoot.getAccountList());
        this.persistedStorage = persisted;
    }

    public static class Persisted {
        private AccountStorage topAccountStorage = new AccountStorage();
        private AccountStorage accountStorage = new AccountStorage();
    }
}
