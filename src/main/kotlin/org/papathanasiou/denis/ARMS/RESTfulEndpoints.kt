package org.papathanasiou.denis.ARMS

import javax.ws.rs.*
import javax.ws.rs.core.Context
import javax.ws.rs.core.MediaType.APPLICATION_JSON
import javax.ws.rs.core.Response
import javax.ws.rs.core.UriInfo

class BadAPIRequest : WebApplicationException {
    constructor(message: String?) : super(message, Response.Status.BAD_REQUEST)
}

@Path("")
class RESTfulEndpoints(config: ARMSConfiguration): MongoInterface {
    override val connection: MongoConnection = MongoConnection(config.mongoURI)

    @GET @Produces(APPLICATION_JSON)
    @Path("{database}/{collection}")
    fun findDocument(@PathParam("database") database: String,
                     @PathParam("collection") collection: String,
                     @Context ui: UriInfo): String? {

        val query = ui.getQueryParameters()
        if( query.isEmpty() ) throw BadAPIRequest("please provide query parameters")

        // simple reply for now
        return "{'db': '$database', 'coll': '$collection'}"
    }
}