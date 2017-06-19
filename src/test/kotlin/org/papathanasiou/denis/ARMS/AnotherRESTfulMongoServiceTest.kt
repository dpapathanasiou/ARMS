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
import javax.ws.rs.core.Response


class AnotherRESTfulMongoServiceTest : JerseyTest() {
    val TEST_DB = "testDB"

    override fun getTestContainerFactory(): TestContainerFactory {
        return GrizzlyWebTestContainerFactory()
    }

    override fun configureDeployment(): DeploymentContext {
        return ServletDeploymentContext.forServlet(ServletContainer(ResourceConfig(RESTfulEndpointsTest::class.java))).build()
    }

    @Test
    fun testInvalidSearch() {
        val result = target(TEST_DB+"/foo").request().get(Response::class.java)
        Assert.assertEquals(Response.Status.BAD_REQUEST.statusCode, result.statusInfo.statusCode)
    }

    @Test
    fun testDocumentNotFound() {
        val result = target(TEST_DB+"/foo").queryParam("id", "bar").request().get(Response::class.java)
        Assert.assertEquals("{}", result.readEntity(String::class.java))
        Assert.assertEquals(Response.Status.NOT_FOUND.statusCode, result.statusInfo.statusCode)
    }

}
