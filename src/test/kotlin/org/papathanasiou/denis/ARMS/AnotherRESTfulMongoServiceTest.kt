package org.papathanasiou.denis.ARMS

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.junit.Assert
import org.junit.Test
import org.glassfish.jersey.test.JerseyTest
import org.glassfish.jersey.server.ResourceConfig
import org.glassfish.jersey.test.grizzly.GrizzlyWebTestContainerFactory
import org.glassfish.jersey.test.spi.TestContainerFactory
import org.glassfish.jersey.servlet.ServletContainer
import org.glassfish.jersey.test.ServletDeploymentContext
import org.glassfish.jersey.test.DeploymentContext
import java.io.IOException

import javax.ws.rs.client.Entity
import javax.ws.rs.core.HttpHeaders
import javax.ws.rs.core.Response


class AnotherRESTfulMongoServiceTest : JerseyTest() {
    val TEST_TARGET = "testDB/docs" // database/collection

    override fun getTestContainerFactory(): TestContainerFactory {
        return GrizzlyWebTestContainerFactory()
    }

    override fun configureDeployment(): DeploymentContext {
        return ServletDeploymentContext.forServlet(ServletContainer(ResourceConfig(RESTfulEndpointsTest::class.java))).build()
    }

    // load the test configuration from src/test/resources
    val mapper = jacksonObjectMapper()
    val json = AnotherRESTfulMongoService.javaClass.classLoader.getResource(CONF_JSON)
            ?: throw IOException("Could not find or load $CONF_JSON from src/test/resources")
    val conf = mapper.readValue<ARMSConfiguration>(json)

    val API_KEY = "API-KEY"
    val API_TOTP = "API-TOTP"

    val userOne = "client1" // read only (find, but not insert nor delete)
    val userTwo = "client2" // all permissions

    @Test
    fun testInvalidSearch() {
        val result = target(TEST_TARGET).request().get(Response::class.java)
        // API KEY and TOTP missing from header, so expect it to return a 'forbidden' result
        Assert.assertEquals(Response.Status.FORBIDDEN.statusCode, result.statusInfo.statusCode)
    }

    @Test
    fun testDocumentNotFound() {
        val totp = RESTfulEndpointsTestAPI.testTOTP(conf.authSeeds.getValue(userTwo).seed).toString(10)
        val result = target(TEST_TARGET).queryParam("id", "bar").request().header(API_KEY, userTwo).header(API_TOTP, totp).get(Response::class.java)
        Assert.assertEquals("{}", result.readEntity(String::class.java))
        Assert.assertEquals(Response.Status.NOT_FOUND.statusCode, result.statusInfo.statusCode)
    }

    @Test
    fun testDocumentInsertForbidden() {
        // use the test document (in src/test/resources)
        val glossary = this.javaClass.classLoader.getResource("glossaryExampleDoc.json").readText()

        val totp = RESTfulEndpointsTestAPI.testTOTP(conf.authSeeds.getValue(userOne).seed).toString(10)
        val result = target(TEST_TARGET).request().header(API_KEY, userOne).header(API_TOTP, totp).put(Entity.json(glossary), Response::class.java)
        // userOne does not have permission to insert
        Assert.assertEquals(Response.Status.FORBIDDEN.statusCode, result.statusInfo.statusCode)
    }

    @Test
    fun testDocumentInsertAndFind() {
        // use the test document (in src/test/resources)
        val glossary = this.javaClass.classLoader.getResource("glossaryExampleDoc.json").readText()

        // userTwo has all permissions, including insert and find
        val totp = RESTfulEndpointsTestAPI.testTOTP(conf.authSeeds.getValue(userTwo).seed).toString(10)
        val insertResult = target(TEST_TARGET).request().header(API_KEY, userTwo).header(API_TOTP, totp).put(Entity.json(glossary), Response::class.java)
        Assert.assertEquals(Response.Status.NO_CONTENT.statusCode, insertResult.statusInfo.statusCode)

        val findResult = target(TEST_TARGET).queryParam("glossary.title", "example glossary").request().header(API_KEY, userTwo).header(API_TOTP, totp).get(Response::class.java)
        Assert.assertEquals(Response.Status.OK.statusCode, findResult.statusInfo.statusCode)
    }

    @Test
    fun testDocumentDelete() {
        val totp = RESTfulEndpointsTestAPI.testTOTP(conf.authSeeds.getValue(userTwo).seed).toString(10)
        val result = target(TEST_TARGET).queryParam("glossary.title", "example glossary").request().header(API_KEY, userTwo).header(API_TOTP, totp).delete(Response::class.java)
        Assert.assertEquals(Response.Status.OK.statusCode, result.statusInfo.statusCode)
        Assert.assertEquals("""{"deleted": "1 document"}""", result.readEntity(String::class.java))
    }

    @Test
    fun testDocumentDeleteForbidden() {
        val totp = RESTfulEndpointsTestAPI.testTOTP(conf.authSeeds.getValue(userOne).seed).toString(10)
        val result = target(TEST_TARGET).queryParam("glossary.title", "example glossary").request().header(API_KEY, userOne).header(API_TOTP, totp).delete(Response::class.java)
        // userOne does not have permission to delete
        Assert.assertEquals(Response.Status.FORBIDDEN.statusCode, result.statusInfo.statusCode)
    }
}
