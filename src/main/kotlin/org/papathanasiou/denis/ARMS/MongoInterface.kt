package org.papathanasiou.denis.ARMS

import com.mongodb.client.MongoCollection

interface MongoInterface {
    val connection: MongoConnection

    fun getCollection(database: String, collection: String): MongoCollection<org.bson.Document>? {
        return connection.getConnection()?.getDatabase(database)?.getCollection(collection)
    }
}
