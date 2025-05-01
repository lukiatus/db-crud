package org.example;

import org.xmldb.api.DatabaseManager;
import org.xmldb.api.base.Collection;
import org.xmldb.api.modules.XMLResource;

import java.io.File;

public class DatabaseSeeder {
    public static void seed() {
        try {
            Collection collection = DatabaseManager.getCollection(Config.URI + Config.COLLECTION_PATH, Config.USER, Config.PASSWORD);
            if(collection == null) {

                System.out.println("❌ Collection does not exist! Create it first!");
                return;
            }

            File folder = new File(Config.LOCAL_DIRECTORY);
            File[] files = folder.listFiles((dir, name) -> name.endsWith(".xml"));

            if (files != null) {
                for (File file : files) {
                    uploadXML(collection, file);
                }
            } else {
                System.out.println("ERR: XML files not found!");
            }

            System.out.println("\nSUCCESS: All XML files uploaded successfully!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void uploadXML(Collection collection, File file) throws Exception {
        XMLResource document = (XMLResource) collection.createResource(file.getName(), "XMLResource");
        document.setContent(file);
        collection.storeResource(document);
        System.out.println(" - Stored: " + file.getName());
    }
}
