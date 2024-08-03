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

import io.helidon.microprofile.testing.junit5.HelidonTest;
import jakarta.inject.Inject;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.Response;
import org.apache.velocity.util.StringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.logging.Logger;

import static java.nio.file.StandardOpenOption.CREATE_NEW;
import static org.os890.microreport.demo.data.sample.SampleDataCreator.initDemoTestdata;

@HelidonTest
public class MicroReportTest {
    private static final Logger LOG = Logger.getLogger("demo");

    @Inject
    private WebTarget target;

    @Inject
    private TestMicroStore testMicroStore;

    @BeforeAll
    public static void initTemplatePath() {
        try {
            String templateName = "generic.vm";
            File baseDirectory = new File(MicroReportTest.class.getResource("/" + templateName).toURI()).getParentFile();
            System.setProperty("reportTemplatePath", baseDirectory.getAbsolutePath());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void overviewRun(@TempDir Path output) throws IOException {
        initTestData(output);

        try (Response response = target.path("micro-report")
                .queryParam("overview", Boolean.TRUE.toString())
                .request().get()) {
            Assertions.assertEquals(200, response.getStatus());
            String microReport = response.readEntity(String.class);
            Assertions.assertNotNull(microReport);

            File targetFile = new File(StringUtils.normalizePath(output.toFile().getAbsolutePath() + "/result_overview.html"));
            writeToFile(targetFile, microReport);
        }
    }

    @Test
    public void fullRun(@TempDir Path output) throws IOException {
        initTestData(output);

        try (Response response = target.path("micro-report").request().get()) {
            Assertions.assertEquals(200, response.getStatus());
            String microReport = response.readEntity(String.class);
            Assertions.assertNotNull(microReport);

            File targetFile = new File(StringUtils.normalizePath(output.toFile().getAbsolutePath() + "/result_overview.html"));
            writeToFile(targetFile, microReport);
        }
    }

    private void initTestData(Path output) {
        testMicroStore.usePath(output);

        //here we would have specific data for the test -- but in the demo we just reuse the generated data
        initDemoTestdata(testMicroStore.getStorageManager(), testMicroStore, testMicroStore.getStorageRoot(), 2, 5);
    }

    //only needed for a quick check with @TempDir(cleanup = CleanupMode.NEVER)
    private static void writeToFile(File targetFile, String microReport) throws IOException {
        Files.writeString(targetFile.toPath(), microReport, CREATE_NEW);
        LOG.info("file path: " + targetFile.getAbsolutePath());
    }
}
