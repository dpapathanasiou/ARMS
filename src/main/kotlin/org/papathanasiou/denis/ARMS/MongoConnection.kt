package org.papathanasiou.denis.ARMS

import com.mongodb.MongoClientURI
import com.mongodb.MongoClient

class MongoConnection {
    var client = MongoClient()

    constructor(uri: String) {
        this.client = MongoClient(MongoClientURI(uri))
    }

    constructor(client: MongoClient?) {
        this.client = client!!
    }

    fun getConnection(): MongoClient? {
        return this.client
    }
}
