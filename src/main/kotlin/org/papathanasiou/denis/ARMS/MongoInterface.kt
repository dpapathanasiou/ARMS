package org.papathanasiou.denis.ARMS

import org.bson.Document;
import com.mongodb.client.MongoCollection
import com.mongodb.client.model.UpdateOptions

interface MongoInterface {
    val connection: MongoConnection

    fun getCollection(database: String, collection: String): MongoCollection<Document>? {
        return connection.getConnection()?.getDatabase(database)?.getCollection(collection)
    }

    fun getDocuments(database: String, collection: String, query: Map<String,String>): List<String>? {
        return getCollection(database, collection)?.find(Document(query))?.map({ it.toJson() })?.toList()
    }

    fun removeDocuments(database: String, collection: String, query: Map<String,String>): Long? {
        return getCollection(database, collection)?.deleteMany(Document(query))?.deletedCount
    }

    fun addDocument(database: String, collection: String, query: Map<String,String>, doc: String?): Unit {
        if (query.isEmpty())
            getCollection(database, collection)?.insertOne(Document.parse(doc))
        else
            getCollection(database, collection)?.replaceOne(Document(query), Document.parse(doc), UpdateOptions().upsert(true))
    }
}
