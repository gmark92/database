package com.example.database;

import com.example.database.handler.MongoHandler;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.Date;

import static java.util.Arrays.asList;


public class CustomerComplaintManager {
    private final MongoHandler databaseHandler;

    public CustomerComplaintManager(MongoHandler databaseHandler) {
        this.databaseHandler = databaseHandler;
    }
    public void createComplaintsData(){
        databaseHandler.createCollection("complaints");
        MongoCollection<Document> complaints = databaseHandler.getCollection("complaints");
        Document complaint = new Document("_id", new ObjectId());
        complaint.append("customer_name", "John Smith");
        complaint.append("purchase_date", new Date());
        complaint.append("status", ComplaintStatus.OPEN);
        complaint.append("purchased_items", asList(new Document("name", "phone"),
                new Document("name", "phone case")));
        complaints.insertOne(complaint);
    }


}
