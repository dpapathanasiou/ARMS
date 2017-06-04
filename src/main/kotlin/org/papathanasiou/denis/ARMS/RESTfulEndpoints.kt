package org.papathanasiou.denis.ARMS

import javax.ws.rs.*
import javax.ws.rs.core.Context
import javax.ws.rs.core.MediaType.APPLICATION_JSON
import javax.ws.rs.core.MultivaluedMap
import javax.ws.rs.core.Response
import javax.ws.rs.core.UriInfo
import kotlin.reflect.jvm.internal.impl.resolve.scopes.SyntheticScopes

class BadAPIRequest : WebApplicationException {
    constructor(message: String?) : super(message, Response.Status.BAD_REQUEST)
}

@Path("")
class RESTfulEndpoints(config: ARMSConfiguration): MongoInterface {
    override val connection: MongoConnection = MongoConnection(config.mongoURI)

    val EMPTY_JSON = "{}"

    fun scalarizeQueryParameters(query: MultivaluedMap<String, String>): Map<String,String> {
        return query.filter({it.value.isNotEmpty()})
                .entries
                .associateBy({it.key}, {it.value.first()})
    }

    @GET @Produces(APPLICATION_JSON)
    @Path("{database}/{collection}")
    fun findDocument(@PathParam("database") database: String,
                     @PathParam("collection") collection: String,
                     @Context ui: UriInfo): Response {

        val query = ui.getQueryParameters()
        if( query.isEmpty() ) throw BadAPIRequest("please provide query parameters")

        val results = getDocuments(database, collection, scalarizeQueryParameters(query))
        val matches = results?.size ?: 0
        val content = if( matches > 0 ) results?.joinToString(",") else EMPTY_JSON
        val status  = if( matches > 0 ) Response.Status.OK else Response.Status.NOT_FOUND

        return Response.status(status).type(APPLICATION_JSON).entity(content).build()
    }
}