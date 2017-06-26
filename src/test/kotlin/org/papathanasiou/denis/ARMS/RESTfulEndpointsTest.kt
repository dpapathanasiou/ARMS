package org.papathanasiou.denis.ARMS

import com.github.fakemongo.Fongo
import javax.ws.rs.*
import javax.ws.rs.core.Context
import javax.ws.rs.core.Response
import javax.ws.rs.core.UriInfo

object RESTfulEndpointsTestAPI {
    // make sure the same instance of Fongo is used for all the unit tests
    val fongo: Fongo = Fongo("foo")
}

@Path("{database}/{collection}")
class RESTfulEndpointsTest(@PathParam("database") database: String,
                           @PathParam("collection") collection: String) {
    val TEST_DB = database
    val TEST_COL = collection

    val rest = RESTfulEndpoints(RESTfulEndpointsTestAPI.fongo.mongo)

    @GET
    fun findDocument(@Context ui: UriInfo): Response {
        return rest.findDocument(TEST_DB, TEST_COL, ui)
    }

    @DELETE
    fun deleteDocument(@Context ui: UriInfo): Response {
        return rest.deleteDocument(TEST_DB, TEST_COL, ui)
    }

    @PUT
    fun addOrReplaceDocument(document: String, @Context ui: UriInfo): Response {
        return rest.addOrReplaceDocument(TEST_DB, TEST_COL, document, ui)
    }
}