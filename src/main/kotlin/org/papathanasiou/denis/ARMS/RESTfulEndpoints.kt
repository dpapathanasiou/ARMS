package org.papathanasiou.denis.ARMS

import javax.ws.rs.*
import javax.ws.rs.core.MediaType.APPLICATION_JSON

class RESTfulEndpoints {
    @GET @Produces(APPLICATION_JSON)
    @Path("{database}/{collection}")
    fun findDocument(@PathParam("database") database: String,
                     @PathParam("collection") collection: String): String? {
        // simple reply for now
        return "{'db': '$database', 'coll': '$collection'}"
    }
}