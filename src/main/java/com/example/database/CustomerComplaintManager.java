package com.example.database;

import com.example.database.handler.MongoHandler;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.InsertManyOptions;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.json.JSONObject;
import org.json.XML;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import static java.util.Arrays.asList;


public class CustomerComplaintManager {
    private final MongoHandler databaseHandler;

    private final String COLLECTION_NAME = "complaints";

    public CustomerComplaintManager(MongoHandler databaseHandler) {
        this.databaseHandler = databaseHandler;
    }
    public void createComplaintsDataOne(){
        databaseHandler.createCollection(COLLECTION_NAME);
        MongoCollection<Document> complaints = databaseHandler.getCollection(COLLECTION_NAME);
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
        MongoCollection<Document> complaints = databaseHandler.getCollection(COLLECTION_NAME);
        complaints.insertMany((List<Document>) Document.parse(dataString).get(COLLECTION_NAME), new InsertManyOptions().ordered(false));
    }

    public void exportComplaintsToXML(String fileName) throws IOException {
        MongoCollection<Document> complaints = databaseHandler.getCollection(COLLECTION_NAME);
        FindIterable<Document> allComplaints = complaints.find();
        Iterator allComplaintsIterator = allComplaints.iterator();
        StringBuilder complaintsSB = new StringBuilder();
        complaintsSB.append("{\"complaints\" : [");
        while(allComplaintsIterator.hasNext()){
            Document complaint = (Document) allComplaintsIterator.next();
            complaintsSB.append(complaint.toJson());
            if (allComplaintsIterator.hasNext()) {
                complaintsSB.append(",");
            }
        }
        complaintsSB.append("]}");
        String complaintsString = complaintsSB.toString().replace("$", "_");
        JSONObject complaintsJson = new JSONObject(complaintsString);
        String xml = XML.toString(complaintsJson);

        Path path = Paths.get(fileName);
        byte[] strToBytes = xml.getBytes();

        Files.write(path, strToBytes);
    }

}
