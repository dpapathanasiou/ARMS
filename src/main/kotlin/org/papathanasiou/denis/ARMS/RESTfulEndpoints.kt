package org.papathanasiou.denis.ARMS

import com.mongodb.MongoClient
import javax.ws.rs.*
import javax.ws.rs.core.*
import javax.ws.rs.core.MediaType.APPLICATION_JSON

class BadAPIRequest(message: String?) : WebApplicationException(message, Response.Status.BAD_REQUEST)
class ForbiddenAPIRequest(message: String?) : WebApplicationException(message, Response.Status.FORBIDDEN)

@Path("")
class RESTfulEndpoints(client: MongoClient?,
                       useAuth: Boolean,
                       auth: Map<String, ClientAuth>,
                       calculateTOTP: (String) -> Int): MongoInterface {

    override val connection: MongoConnection = MongoConnection(client)

    val checkAuth: Boolean = useAuth
    val authorizedClients: Map<String, ClientAuth> = auth
    val computeTOTP: (String) -> Int = calculateTOTP

    val MISSING_PARAMS = "please provide query parameters"
    val API_KEY = "API-KEY"
    val API_TOTP = "API-TOTP"
    val INVALID_TOTP = "please provide a valid $API_KEY and $API_TOTP in the request headers"

    val HTTP_GET = "GET"
    val HTTP_PUT = "PUT"
    val HTTP_DELETE = "DELETE"

    fun isVerbAllowed(clientAuth: ClientAuth?, verb: String): Boolean {
        val result = when(verb) {
            HTTP_GET -> clientAuth?.GET
            HTTP_PUT -> clientAuth?.PUT
            HTTP_DELETE -> clientAuth?.DELETE
            else -> false
        }
        return if( result != null ) result else false
    }

    fun isRequestAuthorized(headers: HttpHeaders, verb: String): Boolean {
        if( checkAuth ) {
            val key = headers.getRequestHeader(API_KEY)?.firstOrNull()
            val pwd = headers.getRequestHeader(API_TOTP)?.firstOrNull()
            if( key != null && pwd != null ) {
                val seed = authorizedClients.get(key)?.seed
                val totp = if( seed != null ) computeTOTP(seed) else null
                val code = if( totp != null ) totp == pwd.toIntOrNull() else false
                return code && isVerbAllowed(authorizedClients.get(key), verb)
            }
            return false
        }
        return true
    }

    fun scalarizeQueryParameters(query: MultivaluedMap<String, String>): Map<String,String> {
        return query.filter({it.value.isNotEmpty()})
                .entries
                .associateBy({it.key}, {it.value.first()})
    }

    @GET @Produces(APPLICATION_JSON)
    @Path("{database}/{collection}")
    fun findDocument(@PathParam("database") database: String,
                     @PathParam("collection") collection: String,
                     @Context ui: UriInfo,
                     @Context headers: HttpHeaders): Response {

        if( !isRequestAuthorized(headers, HTTP_GET) ) throw ForbiddenAPIRequest(INVALID_TOTP)

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
                       @Context ui: UriInfo,
                       @Context headers: HttpHeaders): Response {

        if( !isRequestAuthorized(headers, HTTP_DELETE) ) throw ForbiddenAPIRequest(INVALID_TOTP)

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
                             @Context ui: UriInfo,
                             @Context headers: HttpHeaders): Response {

        if( !isRequestAuthorized(headers, HTTP_PUT) ) throw ForbiddenAPIRequest(INVALID_TOTP)

        // TODO: if the doc is invalid json, return a 400/Bad Request, not the 500 jersey default
        addDocument(database, collection, scalarizeQueryParameters(ui.queryParameters), doc)
        return Response.status(Response.Status.NO_CONTENT).build()
    }
}