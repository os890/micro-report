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
package org.os890.microreport.demo.data.sample;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import one.microstream.storage.types.StorageManager;
import org.os890.microreport.demo.data.Account;
import org.os890.microreport.demo.storage.DataToStoreCreated;
import org.os890.microreport.demo.storage.MicroStore;
import org.os890.microreport.demo.storage.StorageRoot;

import java.util.List;
import java.util.stream.IntStream;

@ApplicationScoped
public class DataRecorder {
    protected void onDataToStoreCreated(@Observes DataToStoreCreated dataToAdd, MicroStore microStore, StorageRoot storageRoot, StorageManager storageManager) {
        //TODO refactor it + add index creation
        List<Account> newAccounts = dataToAdd.getNewAccountStorage().getAccountList();
        if (microStore.isEmpty()) {
            if (newAccounts.size() > 10) {
                IntStream.range(0, newAccounts.size()).forEach(i -> {
                    if (i < 10) {
                        storageRoot.getTopAccountStorage().getAccountList().add(newAccounts.get(i));
                        storageRoot.getAccountStorage().getAccountList().add(newAccounts.get(i));
                    } else {
                        storageRoot.getAccountStorage().getAccountList().add(newAccounts.get(i));
                    }
                });
            } else {
                storageRoot.getTopAccountStorage().getAccountList().addAll(newAccounts);
                storageRoot.getAccountStorage().getAccountList().addAll(newAccounts);
            }
        } else {
            storageRoot.getAccountStorage().getAccountList().addAll(newAccounts);
        }

        storageManager.setRoot(storageRoot.get());
        storageManager.storeRoot();
    }
}
