package org.papathanasiou.denis.ARMS

import com.github.fakemongo.Fongo
import javax.ws.rs.*
import javax.ws.rs.core.Context
import javax.ws.rs.core.Response
import javax.ws.rs.core.UriInfo

@Path("{database}")
class RESTfulEndpointsTest(@PathParam("database") database: String) {
    val TEST_DB = database
    val rest = RESTfulEndpoints(Fongo(TEST_DB).mongo)

    @GET
    @Path("{collection}")
    fun findDocument(@PathParam("collection") collection: String,
                     @Context ui: UriInfo): Response {
        return rest.findDocument(TEST_DB, collection, ui)
    }

    @DELETE
    @Path("{collection}")
    fun deleteDocument(@PathParam("collection") collection: String,
                       @Context ui: UriInfo): Response {
        return rest.deleteDocument(TEST_DB, collection, ui)
    }

    @PUT
    @Path("{collection}")
    fun addOrReplaceDocument(@PathParam("collection") collection: String,
                             document: String,
                             @Context ui: UriInfo): Response {
        return rest.addOrReplaceDocument(TEST_DB, collection, document, ui)
    }
}