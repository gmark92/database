package com.example.database;

public class App {
    public static void main(String[] args) {
        System.out.println(PropertiesCache.getInstance().getProperty("mongodb.uri"));

    }
}
