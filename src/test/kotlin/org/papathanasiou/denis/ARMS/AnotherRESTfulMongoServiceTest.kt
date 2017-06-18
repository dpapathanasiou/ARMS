package org.papathanasiou.denis.ARMS

import com.github.fakemongo.junit.FongoRule
import org.junit.Assert
import org.junit.Test
import org.glassfish.jersey.test.JerseyTest
import org.glassfish.jersey.server.ResourceConfig
import org.glassfish.jersey.test.grizzly.GrizzlyWebTestContainerFactory
import org.glassfish.jersey.test.spi.TestContainerFactory
import org.glassfish.jersey.servlet.ServletContainer
import org.glassfish.jersey.test.ServletDeploymentContext
import org.glassfish.jersey.test.DeploymentContext
import org.junit.Rule

class AnotherRESTfulMongoServiceTest : JerseyTest() {
    @Rule @JvmField
    var fongoRule = FongoRule()

    override fun getTestContainerFactory(): TestContainerFactory {
        return GrizzlyWebTestContainerFactory()
    }

    override fun configureDeployment(): DeploymentContext {
        return ServletDeploymentContext.forServlet(ServletContainer(ResourceConfig(AnotherRESTfulMongoService.javaClass))).build()
    }

    @Test
    fun firstTest() {
        Assert.assertTrue(1 == 1)
    }

}
