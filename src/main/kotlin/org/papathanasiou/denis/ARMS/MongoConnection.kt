package org.papathanasiou.denis.ARMS

import com.mongodb.MongoClientURI
import com.mongodb.MongoClient

class MongoConnection (uri: String) {
    val client = MongoClient(MongoClientURI(uri))

    fun getConnection(): MongoClient? {
        return this.client
    }
}
