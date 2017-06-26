package org.papathanasiou.denis.ARMS

import org.junit.Assert
import org.junit.Test
import org.glassfish.jersey.test.JerseyTest
import org.glassfish.jersey.server.ResourceConfig
import org.glassfish.jersey.test.grizzly.GrizzlyWebTestContainerFactory
import org.glassfish.jersey.test.spi.TestContainerFactory
import org.glassfish.jersey.servlet.ServletContainer
import org.glassfish.jersey.test.ServletDeploymentContext
import org.glassfish.jersey.test.DeploymentContext

import javax.ws.rs.client.Entity
import javax.ws.rs.core.Response


class AnotherRESTfulMongoServiceTest : JerseyTest() {
    val TEST_TARGET = "testDB/docs" // database/collection

    override fun getTestContainerFactory(): TestContainerFactory {
        return GrizzlyWebTestContainerFactory()
    }

    override fun configureDeployment(): DeploymentContext {
        return ServletDeploymentContext.forServlet(ServletContainer(ResourceConfig(RESTfulEndpointsTest::class.java))).build()
    }

    @Test
    fun testInvalidSearch() {
        val result = target(TEST_TARGET).request().get(Response::class.java)
        Assert.assertEquals(Response.Status.BAD_REQUEST.statusCode, result.statusInfo.statusCode)
    }

    @Test
    fun testDocumentNotFound() {
        val result = target(TEST_TARGET).queryParam("id", "bar").request().get(Response::class.java)
        Assert.assertEquals("{}", result.readEntity(String::class.java))
        Assert.assertEquals(Response.Status.NOT_FOUND.statusCode, result.statusInfo.statusCode)
    }

    @Test
    fun testDocumentInsertAndFind() {
        // use the test document (in src/test/resources)
        val glossary = this.javaClass.classLoader.getResource("glossaryExampleDoc.json").readText()

        val insertResult = target(TEST_TARGET).request().put(Entity.json(glossary), Response::class.java)
        Assert.assertEquals(Response.Status.NO_CONTENT.statusCode, insertResult.statusInfo.statusCode)

        val findResult = target(TEST_TARGET).queryParam("glossary.title", "example glossary").request().get(Response::class.java)
        Assert.assertEquals(Response.Status.OK.statusCode, findResult.statusInfo.statusCode)
    }

    @Test
    fun testDocumentDelete() {
        val result = target(TEST_TARGET).queryParam("glossary.title", "example glossary").request().delete(Response::class.java)
        Assert.assertEquals(Response.Status.OK.statusCode, result.statusInfo.statusCode)
        Assert.assertEquals("""{"deleted": "1 document"}""", result.readEntity(String::class.java))
    }
}
