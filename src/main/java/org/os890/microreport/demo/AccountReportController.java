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
package org.os890.microreport.demo;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.os890.microreport.GenericReportEntry;
import org.os890.microreport.demo.data.UserAccountReport;
import org.os890.microreport.demo.storage.MicroStore;

import java.util.List;

@ApplicationScoped
public class AccountReportController {
    private static final String ID_NAME = "ID";
    private static final String GROUP_NAME = "User-Type";

    @Inject
    private MicroStore microStore;

    public List<GenericReportEntry> createTopReports() {
        return microStore.topAccountStorage().getAccountList().stream()
                .map(account -> new GenericReportEntry(new UserAccountReport(account), ID_NAME, GROUP_NAME))
                .toList();
    }

    public List<GenericReportEntry> createAllReports() {
        return microStore.accountStorage().getAccountList().stream()
                .map(account -> new GenericReportEntry(new UserAccountReport(account), ID_NAME, GROUP_NAME))
                .toList();
    }
}
