package org.papathanasiou.denis.ARMS

import javax.ws.rs.core.Application

class JaxRSApplication (restAPI: RESTfulEndpoints) : Application() {
    val rest = restAPI
    override fun getSingletons(): MutableSet<Any> {
        return mutableSetOf(rest)
    }
}
