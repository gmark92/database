package com.example.database;

import com.example.database.handler.MongoHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class App {
    public static void main(String[] args) {
        MongoHandler mongoHandler = new MongoHandler();
        mongoHandler.connectToDatabase("complaints_data");
        CustomerComplaintManager manager = new CustomerComplaintManager(mongoHandler);
        String dataString;
        try {
            dataString = manager.readComplaintsDataFromJson();
            _logger.info("Complaints test data is successfully read");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        manager.createComplaintsDataFromJsonString(dataString);

        try {
            manager.exportComplaintsToXML("complaints.xml");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        _logger.info("Complaints data is successfully exported to XML");

        mongoHandler.printDatabaseStats();
    }

    private static final Logger _logger = LoggerFactory.getLogger(App.class.getName());
}
