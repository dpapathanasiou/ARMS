package org.papathanasiou.denis.ARMS

import org.glassfish.jersey.netty.httpserver.NettyHttpContainerProvider
import org.glassfish.jersey.server.ResourceConfig
import java.net.URI
import javax.ws.rs.ext.ContextResolver

object AnotherRESTfulMongoService {
    @JvmStatic
    fun main(args: Array<String>) {
        val resourceConfig = ResourceConfig.forApplication(JaxRSApplication())
        val server = NettyHttpContainerProvider.createHttp2Server(URI.create("http://localhost:8080/"), resourceConfig, null)
        Runtime.getRuntime().addShutdownHook(Thread(Runnable { server.close() }))
    }
}