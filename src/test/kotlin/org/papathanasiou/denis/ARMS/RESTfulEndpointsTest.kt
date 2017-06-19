package org.papathanasiou.denis.ARMS

import com.github.fakemongo.Fongo
import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.PathParam
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

}