package com.example.database;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Scanner;

public class App {
    public static void main(String[] args) {
        String connectionString = PropertiesCache.getInstance().getProperty("mongodb.uri");
        CustomerComplaintManager manager = new CustomerComplaintManager(connectionString, "complaints_db");
        Scanner scanner = new Scanner(System.in);

        System.out.println("Press enter to upload test data");
        scanner.nextLine();
        uploadTestData(manager);

        System.out.println("Press enter to update complaints status");
        scanner.nextLine();
        updateDataStatus(manager);

        System.out.println("Press enter to export data into XML");
        scanner.nextLine();
        exportDataToXML(manager);
    }

    private static void uploadTestData(CustomerComplaintManager manager){
        String dataString;
        try {
            dataString = manager.readComplaintsDataFromJson();
            _logger.info("Complaints test data is successfully read");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        var result= manager.createComplaintsDataFromJsonString(dataString);
        if (result.wasAcknowledged()){
            _logger.info("Complaints test data was acknowledged by database");
        }
    }

    private static void updateDataStatus(CustomerComplaintManager manager){
        try {
            var result = manager.closeAllComplaints();
            _logger.info("Closed complaints count: " + result.getModifiedCount());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static void exportDataToXML(CustomerComplaintManager manager){
        try {
            manager.exportComplaintsToXML("complaints.xml");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        _logger.info("Complaints data is successfully exported to XML");
    }

    private static final Logger _logger = LoggerFactory.getLogger(App.class.getName());
}
