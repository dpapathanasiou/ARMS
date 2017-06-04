package org.papathanasiou.denis.ARMS

import javax.ws.rs.core.Application

class JaxRSApplication (config: ARMSConfiguration) : Application() {
    val rest = RESTfulEndpoints(config)
    override fun getSingletons(): MutableSet<Any> {
        return mutableSetOf(rest)
    }
}
