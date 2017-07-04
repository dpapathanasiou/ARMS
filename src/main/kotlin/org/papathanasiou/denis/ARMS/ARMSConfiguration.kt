package org.papathanasiou.denis.ARMS

// definition of the ARMSConfiguration file (expected to be in src/main/resources)
val CONF_JSON = "configuration.json"

/**
 * Configuration information for this service:
 *  - URI for the service (host:port)
 *  - MongoConnection URI
 *  - Use Authentication on client requests?
 *  - Authentication: Map<ClientId, ClientAuth>
 */
data class ARMSConfiguration (val mongoURI: String,
                              val serviceURI: String,
                              val authenticate: Boolean,
                              val authSeeds: Map<String, ClientAuth>)