package com.example.database;

import com.example.database.handler.MongoHandler;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.InsertManyOptions;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;

import static java.util.Arrays.asList;


public class CustomerComplaintManager {
    private final MongoHandler databaseHandler;

    public CustomerComplaintManager(MongoHandler databaseHandler) {
        this.databaseHandler = databaseHandler;
    }
    public void createComplaintsDataOne(){
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

    public String readComplaintsDataFromJson() throws IOException {
        InputStream is = getClass().getClassLoader().getResourceAsStream("test_data.json");
        ByteArrayOutputStream result = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        for (int length; (length = is.read(buffer)) != -1; ) {
            result.write(buffer, 0, length);
        }
        return result.toString(StandardCharsets.UTF_8.name());
    }

    public void createComplaintsDataFromJsonString(String dataString) {
        MongoCollection<Document> complaints = databaseHandler.getCollection("complaints");
        complaints.insertMany((List<Document>) Document.parse(dataString).get("complaints"), new InsertManyOptions().ordered(false));
    }

}
