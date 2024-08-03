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
package org.os890.microreport.demo.data;

import jakarta.json.bind.annotation.JsonbProperty;
import org.os890.microreport.Detail;
import org.os890.microreport.Summary;

import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;

public class UserAccountReport {
    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.uuuu hh:mm:ss");
    public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.uuuu");

    private final Account account;

    public UserAccountReport(Account account) {
        this.account = account;
    }

    @JsonbProperty("ID")
    public String getId() {
        return account.getId();
    }

    @Summary(order = 1)
    @JsonbProperty("User-Type")
    public String getUserType() {
        return account.getUserType().name().charAt(0) + account.getUserType().name().substring(1).toLowerCase();
    }

    @Summary(order = 2)
    @Detail(order = 1)
    @JsonbProperty("Username")
    public String getUserName() {
        return account.getUserName();
    }

    @Summary(order = 3)
    @Detail(order = 2)
    @JsonbProperty("Birthday")
    public String getBirthday() {
        return account.getPerson().getBirthday().format(DATE_FORMATTER);
    }

    @Summary(order = 4)
    @Detail(order = 3)
    @JsonbProperty("E-Mail")
    public String getEmail() {
        return account.getEmail();
    }


    @Detail(order = 4)
    @JsonbProperty("Firstname")
    public String getFirstName() {
        return account.getPerson().getFirstName();
    }

    @Detail(order = 5)
    @JsonbProperty("Lastname")
    public String getLastName() {
        return account.getPerson().getLastName();
    }

    @Detail(order = 6)
    @JsonbProperty("Registered at")
    public String getRegisteredAt() {
        return account.getRegisteredAt().format(DATE_TIME_FORMATTER).replace(" ", "<br/>");
    }

    @Detail(order = 7)
    @JsonbProperty("PW-Hint")
    public String getHint() {
        return account.getPwHint();
    }

    @Detail(order = 8)
    @JsonbProperty("Notes")
    public String getNotes() {
        return account.getNotes().stream().collect(Collectors.joining("<br/>"));
    }
}
