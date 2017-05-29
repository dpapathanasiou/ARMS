package org.papathanasiou.denis.ARMS

import javax.ws.rs.core.Application

class JaxRSApplication: Application() {
    override fun getSingletons(): MutableSet<Any> {
        return mutableSetOf(RESTfulEndpoints())
    }
}
