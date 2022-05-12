package com.example.database;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.Set;

public class PropertiesCache
{
    private final Properties configProp = new Properties();

    private PropertiesCache()
    {
        try (InputStream input = this.getClass().getClassLoader().getResourceAsStream("config.properties")) {
            if (input == null) {
                System.out.println("Sorry, unable to find config.properties");
            }
            configProp.load(input);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static class LazyHolder
    {
        private static final PropertiesCache INSTANCE = new PropertiesCache();
    }

    public static PropertiesCache getInstance()
    {
        return LazyHolder.INSTANCE;
    }

    public String getProperty(String key){
        return configProp.getProperty(key);
    }

    public Set<String> getAllPropertyNames(){
        return configProp.stringPropertyNames();
    }

    public boolean containsKey(String key){
        return configProp.containsKey(key);
    }
}
