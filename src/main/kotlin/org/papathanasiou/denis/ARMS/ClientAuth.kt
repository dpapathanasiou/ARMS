package org.papathanasiou.denis.ARMS

/**
 * Defines the client seed string for authentication, along with which http verbs are allowed.
 */
data class ClientAuth (val seed: String, val GET: Boolean, val PUT: Boolean, val DELETE: Boolean)