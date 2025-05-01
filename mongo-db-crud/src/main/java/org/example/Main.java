package org.example;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.*;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.Projections;
import org.bson.Document;

import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {
    private static MongoDatabase mongoDatabase;

    public static void main(String[] args) throws Exception {
        var mongoLogger = Logger.getLogger("org.mongodb.driver");
        mongoLogger.setLevel(Level.OFF);

        var mongoClient = MongoClients.create(
                MongoClientSettings.builder()
                        .applyConnectionString(new ConnectionString(Config.URI))
                        .build());

        mongoDatabase = mongoClient.getDatabase(Config.DATABASE);

        System.out.println("\n\n========== Seed database with data ==========");
        DatabaseSeeder.seed(mongoDatabase);
        System.out.println("=============================================\n");

        System.out.println("\n\n***** List all musicians *****");
        listMusicians();

        var email = "emily.white@example.com";
        System.out.println("\n\n***** Get musician by email: " + email + " *****");
        getMusicianByEmail(email);

        System.out.println("\n\n***** List the members of each band *****");
        listBandMembers();

        System.out.println("\n\n***** List the musicians average age for each band *****");
        listAverageAgePerBand();

        System.out.println("\n\n***** What is the name of the venue where the last concert will be held and what is the email address of the main band? *****");
        getLastConcertMainBandLeaderEmail();

        var musicianIdToEdit = 4;
        System.out.println("\n\n***** Update the name of musician with ID = " + musicianIdToEdit + " *****");
        updateMusician(musicianIdToEdit);

        var musicianIdToDelete = 10;
        System.out.println("\n\n***** Delete the musician with ID = " + musicianIdToDelete + " *****");
        deleteMusicianById(musicianIdToDelete);

        System.out.println("\n\n***** List all musicians after modifications *****");
        listMusicians();

        mongoClient.close();
    }

    private static void listAverageAgePerBand() {
        var collection = mongoDatabase.getCollection("Bands");
        var pipeline = List.of(
                Aggregates.project(Projections.fields(
                        Projections.include("Name"),
                        Projections.computed("AverageAge", new Document("$avg", new Document("$map", new Document()
                                .append("input", "$Musicians")
                                .append("as", "m")
                                .append("in", new Document("$subtract", Arrays.asList(2025, "$$m.BirthYear")))
                        )))
                ))
        );

        AggregateIterable<Document> results = collection.aggregate(pipeline);

        for (Document doc : results) {
            String name = doc.getString("Name");
            Double avgAge = doc.getDouble("AverageAge");
            System.out.println(name + ": " + Math.round(avgAge));
        }
    }

    private static void getLastConcertMainBandLeaderEmail() {
        var concertsCollection = mongoDatabase.getCollection("Concerts");
        var bandsCollection = mongoDatabase.getCollection("Bands");

        var lastConcert = concertsCollection.find().sort(new Document("Date", -1)).first();
        var venueName = lastConcert.get("Place", Document.class).getString("Name");
        var headLinerBandId = lastConcert.get("HeadlinerBand", Document.class).getInteger("_id");

        var headLinerBand = bandsCollection.find(new Document("_id", headLinerBandId)).first();
        var leaderEmail = headLinerBand.get("LeaderMusician", Document.class).getString("Email");

        System.out.println("Venue: " + venueName);
        System.out.println("Main band email: " + leaderEmail);
    }

    private static void deleteMusicianById(int musicianIdToDelete) {
        var collection = mongoDatabase.getCollection("Musicians");
        var filter = new Document("_id", musicianIdToDelete);
        collection.deleteOne(filter);
    }

    private static void updateMusician(int musicianIdToEdit) {
        var collection = mongoDatabase.getCollection("Musicians");
        var filter = new Document("_id", musicianIdToEdit);
        var band = collection.find(filter).first();
        var updateName = new Document("$set", new Document("Name", band.get("Name") + " Edited"));
        var updateMail = new Document("$set", new Document("Email", "edited" + band.get("Email")));
        collection.updateOne(filter, updateName);
        collection.updateOne(filter, updateMail);
    }

    private static void listBandMembers() {
        var collection = mongoDatabase.getCollection("Bands");

        for (var band : collection.find()) {
            System.out.println("Band Name: " + band.getString("Name"));
            System.out.println("Leader: " + band.getEmbedded(Arrays.asList("LeaderMusician", "Name"), String.class));

            var musicians = (List<Document>) band.get("Musicians");
            System.out.print("Members:");
            System.out.println(String.join(", ", musicians.stream()
                    .map(musician -> musician.getString("Name"))
                    .toArray(String[]::new)));

            System.out.println();
        }
    }

    private static void getMusicianByEmail(String email) {
        var collection = mongoDatabase.getCollection("Musicians");
        var musician = collection.find(new Document("Email", email)).first();
        System.out.println(musician.toJson());
    }

    private static void listMusicians() {
        var collection = mongoDatabase.getCollection("Musicians");
        var documents = collection.find();
        for (Document doc : documents) {
            System.out.println(doc.toJson());
        }
    }
}