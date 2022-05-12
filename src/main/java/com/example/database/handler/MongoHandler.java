package com.example.database.handler;

import com.example.database.PropertiesCache;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.BsonDocument;
import org.bson.BsonInt64;
import org.bson.Document;
import org.bson.conversions.Bson;

public class MongoHandler {
    private final String connectionString;
    private MongoClient mongoClient;
    private MongoDatabase mongoDatabase;

    public MongoHandler(){
        connectionString = PropertiesCache.getInstance().getProperty("mongodb.uri");
    }

    public MongoDatabase connectToDatabase(String databaseName){
        mongoClient = MongoClients.create(connectionString);
        mongoDatabase = mongoClient.getDatabase(databaseName);
        return mongoDatabase;
    }

    public void printDatabaseStats(){
        Bson command = new BsonDocument("dbStats", new BsonInt64(1));
        Document commandResult = mongoDatabase.runCommand(command);
        System.out.println("dbStats: " + commandResult.toJson());
    }

    public void createCollection(String collectionName) {
        mongoDatabase.createCollection(collectionName);
    }

    public MongoCollection<Document> getCollection(String collectionName) {
        return mongoDatabase.getCollection(collectionName);
    }
}
