package oh_heaven.game;

// Based on  PropertiesLoader class from Project 1

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesLoader
{
    public static final String DEF_DIR = "properties/";
    public static Properties loadPropertiesFile(String propertiesFile){
        if (propertiesFile == null){
            try(InputStream input = new FileInputStream(DEF_DIR + "default.properties")){
                Properties properties = new Properties();
                properties.load(input);
                propertiesFile = DEF_DIR + properties.getProperty("default_test");
            } catch (IOException ex){
                ex.printStackTrace();
            }
        }
        try (InputStream input = new FileInputStream(propertiesFile)){
            Properties properties = new Properties();
            properties.load(input);
            return properties;
        } catch (IOException ex){
            ex.printStackTrace();
        }
        return null;
    }


}
