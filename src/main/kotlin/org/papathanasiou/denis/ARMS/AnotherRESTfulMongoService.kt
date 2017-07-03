package org.papathanasiou.denis.ARMS

import org.glassfish.jersey.netty.httpserver.NettyHttpContainerProvider
import org.glassfish.jersey.server.ResourceConfig

import java.net.URI

import com.fasterxml.jackson.module.kotlin.*
import java.io.IOException


object AnotherRESTfulMongoService {
    @JvmStatic
    fun main(args: Array<String>) {
        val mapper = jacksonObjectMapper()
        val json = AnotherRESTfulMongoService.javaClass.classLoader.getResource(CONF_JSON)
                ?: throw IOException("Could not find or load $CONF_JSON from src/main/resources")
        val conf = mapper.readValue<ARMSConfiguration>(json)
        val connection = MongoConnection(conf.mongoURI)
        val resourceConfig = ResourceConfig.forApplication(JaxRSApplication(RESTfulEndpoints(connection.getConnection(), conf.authenticate, conf.authSeeds, TimeBasedOneTimePassword.TOTP)))
        val server = NettyHttpContainerProvider.createHttp2Server(URI.create(conf.serviceURI), resourceConfig, null)
        Runtime.getRuntime().addShutdownHook(Thread(Runnable { server.close() }))
    }
}