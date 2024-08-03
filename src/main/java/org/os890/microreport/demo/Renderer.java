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

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.os890.microreport.GenericReportEntry;

import java.io.File;
import java.io.StringWriter;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

public class Renderer {
    public static String render(List<GenericReportEntry> summaryEntries, List<GenericReportEntry> allEntries, boolean showOverviewOnly, File baseDirectory, String templateName) {
        try (StringWriter writer = new StringWriter()) {
            String absolutePath = baseDirectory.getAbsolutePath();

            VelocityEngine velocityEngine = new VelocityEngine();
            velocityEngine.setProperty(RuntimeConstants.FILE_RESOURCE_LOADER_PATH, absolutePath);
            velocityEngine.init();
            VelocityContext context = new VelocityContext();
            Template template = velocityEngine.getTemplate(templateName, "UTF-8");


            Map<String, List<GenericReportEntry>> grouped = allEntries.stream().collect(groupingBy(GenericReportEntry::getGroupName, toList()));

            context.put("summaryColumns", summaryEntries.stream().flatMap(e -> e.getSummaryColumns().stream()).distinct().collect(toList()));
            context.put("detailColumns", allEntries.stream().flatMap(e -> e.getDetailColumns().stream()).distinct().collect(toList()));

            context.put("summaryList", summaryEntries);

            context.put("groupLabel", summaryEntries.stream().map(GenericReportEntry::getGroupNameLabel).findFirst().orElse("??"));
            context.put("groupNames", grouped.keySet());
            context.put("groupedMap", grouped);

            context.put("showOverview", showOverviewOnly);
            context.put("showDetails", !showOverviewOnly);

            template.merge(context, writer);

            writer.flush();

            String output = writer.toString();
            return output;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
