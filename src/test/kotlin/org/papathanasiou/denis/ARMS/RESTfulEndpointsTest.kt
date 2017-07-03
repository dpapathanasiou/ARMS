package org.papathanasiou.denis.ARMS

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.github.fakemongo.Fongo
import java.io.IOException
import java.util.*
import java.util.concurrent.TimeUnit
import javax.ws.rs.*
import javax.ws.rs.core.Context
import javax.ws.rs.core.HttpHeaders
import javax.ws.rs.core.Response
import javax.ws.rs.core.UriInfo

object RESTfulEndpointsTestAPI {
    // make sure the same instance of Fongo is used for all the unit tests
    val fongo: Fongo = Fongo("foo")
    // use a consistent TOTP generator, to avoid flaky tests
    val testTOTP: (String) -> Int = fun (seed: String): Int {
        return TimeBasedOneTimePassword.generate(seed, 30L, TimeUnit.SECONDS, Date(TimeUnit.SECONDS.toMillis(59L)), "HmacSHA1")
    }
}

@Path("{database}/{collection}")
class RESTfulEndpointsTest(@PathParam("database") database: String,
                           @PathParam("collection") collection: String) {
    val TEST_DB = database
    val TEST_COL = collection

    // load the test configuration from src/test/resources
    val mapper = jacksonObjectMapper()
    val json = AnotherRESTfulMongoService.javaClass.classLoader.getResource(CONF_JSON)
            ?: throw IOException("Could not find or load $CONF_JSON from src/test/resources")
    val conf = mapper.readValue<ARMSConfiguration>(json)
    val rest = RESTfulEndpoints(RESTfulEndpointsTestAPI.fongo.mongo, conf.authenticate, conf.authSeeds, RESTfulEndpointsTestAPI.testTOTP)

    @GET
    fun findDocument(@Context ui: UriInfo, @Context headers: HttpHeaders): Response {
        return rest.findDocument(TEST_DB, TEST_COL, ui, headers)
    }

    @DELETE
    fun deleteDocument(@Context ui: UriInfo, @Context headers: HttpHeaders): Response {
        return rest.deleteDocument(TEST_DB, TEST_COL, ui, headers)
    }

    @PUT
    fun addOrReplaceDocument(document: String, @Context ui: UriInfo, @Context headers: HttpHeaders): Response {
        return rest.addOrReplaceDocument(TEST_DB, TEST_COL, document, ui, headers)
    }
}