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

import org.os890.microreport.demo.data.Account;

import java.util.ArrayList;
import java.util.List;

public class DiskState {
    private List<Account> topAccountList = new ArrayList<>();
    private List<Account> accountList = new ArrayList<>();

    public DiskState(List<Account> topAccountList, List<Account> accountList) {
        this.topAccountList.addAll(topAccountList);
        this.accountList.addAll(accountList);
    }

    public List<Account> getTopAccountList() {
        return topAccountList;
    }

    public List<Account> getAccountList() {
        return accountList;
    }
}
