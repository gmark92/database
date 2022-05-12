package com.example.database;

import com.example.database.handler.MongoHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class App {
    public static void main(String[] args) {
        MongoHandler mongoHandler = new MongoHandler();
        mongoHandler.connectToDatabase("test");
        mongoHandler.printDatabaseStats();


    }
    private static final Logger _logger = LoggerFactory.getLogger(App.class.getName());
}
