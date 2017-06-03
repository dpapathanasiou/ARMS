package org.papathanasiou.denis.ARMS

import javax.ws.rs.*
import javax.ws.rs.core.MediaType.APPLICATION_JSON
import javax.ws.rs.core.Response

class BadAPIRequest : WebApplicationException {
    constructor(message: String?) : super(message, Response.Status.BAD_REQUEST)
}

@Path("")
class RESTfulEndpoints {
    @GET @Produces(APPLICATION_JSON)
    @Path("{database}/{collection}")
    fun findDocument(@PathParam("database") database: String,
                     @PathParam("collection") collection: String): String? {
        // simple reply for now
        return "{'db': '$database', 'coll': '$collection'}"
    }
}