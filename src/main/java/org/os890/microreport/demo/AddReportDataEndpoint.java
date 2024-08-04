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
import jakarta.enterprise.event.Event;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import org.os890.microreport.demo.storage.AccountStorage;
import org.os890.microreport.demo.storage.DataToStoreCreated;

import static jakarta.ws.rs.core.MediaType.TEXT_HTML;
import static jakarta.ws.rs.core.MediaType.TEXT_PLAIN;
import static jakarta.ws.rs.core.Response.Status.FOUND;
import static org.os890.microreport.demo.data.sample.SampleDataCreator.createRandomUsers;

//TODO refactor to sub-resource
@Path("micro-report/more")
@RequestScoped
public class AddReportDataEndpoint {

    @Inject
    private Event<DataToStoreCreated> dataToStoreCreatedEvent;

    @GET
    @Produces(TEXT_HTML)
    public Response createForm(@Context UriInfo uriInfo) {
        String simpleForm = """
                <!DOCTYPE html>
                <html>
                <body>
                	<h1>Create more data</h1>
                	<form action="/${postTarget}" method="post">
                		<p>Number: <input type="text" name="numberOfNewEntries" /></p>
                		<input type="submit" value="Create" />
                	</form>
                </body>
                </html>
                """.replace("${postTarget}", uriInfo.getPath());

        return Response.ok().entity(simpleForm).build();
    }

    @POST
    @Produces(TEXT_HTML)
    public Response createMoreData(@FormParam("numberOfNewEntries") int numberOfNewEntries, @Context UriInfo uriInfo) {
        if (numberOfNewEntries > 100_000) {
            return Response.status(Response.Status.PRECONDITION_FAILED).type(TEXT_PLAIN).entity(numberOfNewEntries + " is too large").build();
        }
        AccountStorage newAccounts = new AccountStorage();
        createRandomUsers(numberOfNewEntries, newAccounts.getAccountList());
        dataToStoreCreatedEvent.fire(new DataToStoreCreated(newAccounts));

        return Response.status(FOUND).location(uriInfo.getBaseUriBuilder().path("/" + MicroReportEndpoint.class.getAnnotation(Path.class).value()).build()).build();
    }

}
