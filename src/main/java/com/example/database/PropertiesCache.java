package com.example.database;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesCache
{
    private final Properties configProp = new Properties();

    private PropertiesCache()
    {
        try (InputStream input = this.getClass().getClassLoader().getResourceAsStream("config.properties")) {
            if (input == null) {
                _logger.error("Sorry, unable to find config.properties");
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

    private static final Logger _logger = LoggerFactory.getLogger(PropertiesCache.class.getName());
}
