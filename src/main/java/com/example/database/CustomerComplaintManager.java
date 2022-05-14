package com.example.database;

import com.mongodb.client.*;
import com.mongodb.client.model.InsertManyOptions;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.InsertManyResult;
import com.mongodb.client.result.UpdateResult;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.json.JSONObject;
import org.json.XML;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.List;

import static com.mongodb.client.model.Filters.ne;


public class CustomerComplaintManager {
    public CustomerComplaintManager (String connectionString, String databaseName){
        mongoClient = MongoClients.create(connectionString);
        mongoDatabase = mongoClient.getDatabase(databaseName);
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

    public InsertManyResult createComplaintsDataFromJsonString(String dataString) {
        MongoCollection<Document> complaints = mongoDatabase.getCollection(COLLECTION_NAME);
        return complaints.insertMany((List<Document>) Document.parse(dataString).get(COLLECTION_NAME), new InsertManyOptions().ordered(false));
    }

    public void exportComplaintsToXML(String fileName) throws IOException {
        MongoCollection<Document> complaints = mongoDatabase.getCollection(COLLECTION_NAME);
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
        String complaintsString = complaintsSB.toString().replace("$", "");
        JSONObject complaintsJson = new JSONObject(complaintsString);
        String xml = XML.toString(complaintsJson);

        Path path = Paths.get(fileName);
        byte[] strToBytes = xml.getBytes();

        Files.write(path, strToBytes);
    }

    public UpdateResult closeAllComplaints(){
        MongoCollection<Document> complaints = mongoDatabase.getCollection(COLLECTION_NAME);
        Bson query = ne("status", ComplaintStatus.CLOSED);
        Bson updates = Updates.set("status", ComplaintStatus.CLOSED);
        return complaints.updateMany(query, updates);
    }

    private final String COLLECTION_NAME = "complaints";
    private final MongoClient mongoClient;
    private final MongoDatabase mongoDatabase;
}
