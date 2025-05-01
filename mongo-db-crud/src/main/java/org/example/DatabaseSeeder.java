package org.example;

import com.fasterxml.jackson.core.type.TypeReference;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.io.File;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;

public class DatabaseSeeder {
    public static void seed(MongoDatabase mongoDatabase) throws Exception {
        try {
            mongoDatabase.drop();

            File folder = new File(Config.LOCAL_DIRECTORY);
            File[] files = folder.listFiles((dir, name) -> name.endsWith(".json"));

            if (files != null) {
                for (File file : files) {
                    String collectionName = file.getName().replace(".json", "");
                    MongoCollection<Document> collection = mongoDatabase.getCollection(collectionName);
                    uploadJSON(collection, file);
                }
                System.out.println("\nSUCCESS: All JSON files uploaded successfully!");

            } else {
                System.out.println("ERR: JSON files not found!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void uploadJSON(MongoCollection<Document> collection, File file) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        List<Document> documents = mapper.readValue(file, new TypeReference<List<Document>>() {
        });
        collection.insertMany(documents);
        System.out.println(" - Stored: " + file.getName());
    }
}
