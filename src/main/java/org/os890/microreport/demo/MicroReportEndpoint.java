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
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import org.os890.microreport.demo.storage.MicroStore;
import org.os890.microreport.demo.storage.StorageRoot;

import static jakarta.ws.rs.core.MediaType.TEXT_HTML;

@Path("micro-report")
@RequestScoped
public class MicroReportEndpoint {

    @Inject
    private AccountReportController accountReportController;

    @Inject
    private GenericReportRunner genericReportRunner;

    @Inject
    private MicroStore microStore;

    @GET
    @Produces(TEXT_HTML)
    public Response overviewReport(@QueryParam("overview") Boolean showOverviewOnly, @Context UriInfo uriInfo) {
        if (microStore.isEmpty()) {
            String noDataResponse = """
                    <html>
                      <head>
                        <meta http-equiv="refresh" content="0;url=#{target}" />
                      </head>
                     <body></body>
                    </html>
                    """.replace("#{target}", uriInfo.getRequestUriBuilder().path("/more").build().toString());
            return Response.ok().entity(noDataResponse).build();
        }

        return Response
                .ok(genericReportRunner.render(accountReportController.createTopReports(), accountReportController.createAllReports(), Boolean.TRUE.equals(showOverviewOnly)))
                .build();
    }
}
