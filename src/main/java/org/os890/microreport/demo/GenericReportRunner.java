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

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.os890.microreport.GenericReportEntry;

import java.io.File;
import java.util.List;

@RequestScoped
public class GenericReportRunner {
    @Inject
    @ConfigProperty(name = "reportTemplatePath")
    private String baseDirectoryPath;

    public String render(List<GenericReportEntry> summaryEntries, List<GenericReportEntry> allEntries, boolean showOverviewOnly) {
        try {
            String templateName = "generic.vm";

            File baseDirectory = new File(baseDirectoryPath);
            return Renderer.render(summaryEntries, allEntries, showOverviewOnly, baseDirectory, templateName);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
