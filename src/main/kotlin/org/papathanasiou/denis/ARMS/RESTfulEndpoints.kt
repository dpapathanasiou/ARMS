package org.papathanasiou.denis.ARMS

import com.mongodb.MongoClient
import javax.ws.rs.DELETE
import javax.ws.rs.GET
import javax.ws.rs.PUT
import javax.ws.rs.Consumes
import javax.ws.rs.Path
import javax.ws.rs.PathParam
import javax.ws.rs.Produces
import javax.ws.rs.WebApplicationException
import javax.ws.rs.core.Context
import javax.ws.rs.core.MediaType.APPLICATION_JSON
import javax.ws.rs.core.MultivaluedMap
import javax.ws.rs.core.Response
import javax.ws.rs.core.UriInfo

class BadAPIRequest(message: String?) : WebApplicationException(message, Response.Status.BAD_REQUEST)

@Path("")
class RESTfulEndpoints(client: MongoClient?): MongoInterface {
    override val connection: MongoConnection = MongoConnection(client)

    val MISSING_PARAMS = "please provide query parameters"

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

        val query = ui.queryParameters
        if( query.isEmpty() ) throw BadAPIRequest(MISSING_PARAMS)

        val results = getDocuments(database, collection, scalarizeQueryParameters(query))
        val matches = results?.size ?: 0
        val content = if( matches > 0 ) results?.joinToString(",") else "{}"
        val status  = if( matches > 0 ) Response.Status.OK else Response.Status.NOT_FOUND

        return Response.status(status).type(APPLICATION_JSON).entity(content).build()
    }

    @DELETE @Produces(APPLICATION_JSON)
    @Path("{database}/{collection}")
    fun deleteDocument(@PathParam("database") database: String,
                       @PathParam("collection") collection: String,
                       @Context ui: UriInfo): Response {

        val query = ui.queryParameters
        if (query.isEmpty()) throw BadAPIRequest(MISSING_PARAMS)

        val results = removeDocuments(database, collection, scalarizeQueryParameters(query))
        val matches = results ?: 0L
        val content = if( matches == 1L ) """{"deleted": "1 document"}""" else """{"deleted": "$matches documents"}"""
        return Response.status(Response.Status.OK).type(APPLICATION_JSON).entity(content).build()
    }

    @PUT @Consumes(APPLICATION_JSON)
    @Path("{database}/{collection}")
    fun addOrReplaceDocument(@PathParam("database") database: String,
                             @PathParam("collection") collection: String,
                             doc: String,
                             @Context ui: UriInfo): Response {

        // TODO: if the doc is invalid json, return a 400/Bad Request, not the 500 jersey default
        addDocument(database, collection, scalarizeQueryParameters(ui.queryParameters), doc)
        return Response.status(Response.Status.NO_CONTENT).build()
    }
}