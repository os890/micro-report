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

import com.github.javafaker.Faker;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import one.microstream.storage.types.StorageManager;
import org.os890.microreport.demo.data.Account;
import org.os890.microreport.demo.data.Person;
import org.os890.microreport.demo.data.UserType;
import org.os890.microreport.demo.storage.MicroStore;
import org.os890.microreport.demo.storage.StorageCreated;
import org.os890.microreport.demo.storage.StorageRoot;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Logger;
import java.util.stream.IntStream;

import static org.os890.microreport.demo.data.UserType.BUSINESS;
import static org.os890.microreport.demo.data.UserType.PRIVATE;

@ApplicationScoped
public class SampleDataCreator {
    private static final Logger LOG = Logger.getLogger(SampleDataCreator.class.getSimpleName());

    private static final Faker DATA_GENERATOR = Faker.instance(Locale.getDefault());

    protected void onStorageCreated(@Observes StorageCreated storageCreated, StorageRoot storageRoot) {
        if (storageCreated.isCreateSampleData()) {
            initDemoTestdata(storageCreated.getStorageManager(), storageCreated.getMicroStore(), storageRoot, 10, 20);
        }
    }

    public static void initDemoTestdata(StorageManager storageManager, MicroStore microStore, StorageRoot storageRoot, int numberOfTopAccounts, int numberOfAdditionalAccounts) {
        LOG.info("start demo-data generation");

        try {
            if (microStore.topAccountStorage().getAccountList().isEmpty()) {
                List<Account> workingList = new CopyOnWriteArrayList<>();

                createRandomUsers(numberOfTopAccounts, workingList);
                microStore.topAccountStorage().getAccountList().addAll(workingList);

                microStore.accountStorage().getAccountList().addAll(workingList);

                workingList.clear();
                createRandomUsers(numberOfAdditionalAccounts, workingList);
                microStore.accountStorage().getAccountList().addAll(workingList);

                //was: microStore.createData();
                storageManager.setRoot(storageRoot.get());
                storageManager.storeRoot();
            }
        } finally {
            LOG.info("demo-data generation finished - number of data-files: " + microStore.fileCount());
        }
    }

    private static void createRandomUsers(int amount, List<Account> dataList) {
        IntStream.range(0, amount).parallel().forEach(entryNumber -> {
            LOG.info("create acount #" + entryNumber);
            Account account = new Account();

            LocalDateTime date = DATA_GENERATOR.date().birthday(20, 70).toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();

            final UserType userType;
            int randomType = DATA_GENERATOR.random().nextInt(2, Integer.MAX_VALUE) % 2;
            if (randomType == PRIVATE.ordinal()) {
                userType = PRIVATE;
            } else {
                userType = BUSINESS;
            }

            DATA_GENERATOR.address().firstName();
            DATA_GENERATOR.address().lastName();
            account.setId(UUID.randomUUID().toString());
            account.setEmail(DATA_GENERATOR.internet().emailAddress());
            account.setUserName(DATA_GENERATOR.name().username());
            account.setUserType(userType);
            account.setPwHint(DATA_GENERATOR.internet().password(2, 4, true, true) + "...");
            account.setRegisteredAt(date.minusDays(DATA_GENERATOR.random().nextInt(10, 1000)));

            IntStream.range(0, 20).forEach(i -> {
                String note = DATA_GENERATOR.commerce().promotionCode(DATA_GENERATOR.random().nextInt(2, 10)).replace("ller", "nd");

                account.getNotes().add("promo-code: " + note + " for " + DATA_GENERATOR.commerce().productName());
            });

            Person person = new Person();
            account.setPerson(person);

            person.setFirstName(DATA_GENERATOR.name().firstName());
            person.setLastName(DATA_GENERATOR.name().lastName());
            person.setBirthday(date.toLocalDate());

            dataList.add(account);
        });
    }
}
