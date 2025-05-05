package org.example;

import org.xmldb.api.DatabaseManager;
import org.xmldb.api.base.Collection;
import org.xmldb.api.base.Database;
import org.xmldb.api.base.XMLDBException;
import org.xmldb.api.modules.XMLResource;
import org.xmldb.api.modules.XPathQueryService;

import java.lang.reflect.InvocationTargetException;

public class Main {
    public static void main(String[] args) throws Exception {
        initializeDriver();

        System.out.println("\n\n========== Seed database with data ==========");
        DatabaseSeeder.seed();
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

    }

    private static void getLastConcertMainBandLeaderEmail() throws XMLDBException {
        Collection collection;
        collection = DatabaseManager.getCollection(Config.URI + Config.COLLECTION_PATH, Config.USER, Config.PASSWORD);
        XPathQueryService service = (XPathQueryService) collection.getService("XPathQueryService", "1.0");

        String query =
                """
                        xquery version "3.1";
                        let $concerts := //Concerts/Concert
                        let $latestConcert := (
                          for $c in $concerts
                          order by xs:date($c/Date) descending
                          return $c
                        )[1]
                        let $concertPlaceId := $latestConcert/PlaceId
                        let $venueName := //Places/Place[@Id = $concertPlaceId]/Name
                        let $headlinerBandId := $latestConcert/HeadlinerBandId
                        let $headlinerBand := //Bands/Band[@Id = $headlinerBandId]
                        let $leaderMusicianId := $headlinerBand/LeaderMusicianId
                        let $leaderMusicianEmail := //Musicians/Musician[@Id = $leaderMusicianId]/Email
                        return
                          <Result>
                            <Venue>{ $venueName }</Venue>
                            <MainBandEmail>{ $leaderMusicianEmail }</MainBandEmail>
                          </Result>
                """;

        var results = service.query(query).getIterator();
        while (results.hasMoreResources()) {
            XMLResource resource = (XMLResource) results.nextResource();
            System.out.println(resource.getContent());
        }
    }

    private static void listBandMembers() throws XMLDBException {
        Collection collection;
        collection = DatabaseManager.getCollection(Config.URI + Config.COLLECTION_PATH, Config.USER, Config.PASSWORD);
        XPathQueryService service = (XPathQueryService) collection.getService("XPathQueryService", "1.0");

        String query =
                """
                        xquery version "3.1";
                        <Result>{
                          for $band in //Bands/Band
                          let $bandId := $band/@Id
                          let $bandName := $band/Name
                          let $leaderMusicianId := $band/LeaderMusicianId
                          let $musicians :=
                            for $bm in //BandMusicians/BandMusician[BandId = $bandId]
                            let $musician := //Musicians/Musician[@Id = $bm/MusicianId]
                            return $musician
                          let $leader := $musicians[@Id = $leaderMusicianId]/Name/text()
                          let $members :=
                            for $m in $musicians
                            where $m/@Id != $leaderMusicianId
                            return $m/Name/text()
                          let $allNames := string-join(($leader, $members), ", ")
                          return
                            <Band>
                              <Name>{ $bandName/text() }</Name>
                              <Members>{ $allNames }</Members>
                            </Band>
                        }</Result>
                """;

        var results = service.query(query).getIterator();
        while (results.hasMoreResources()) {
            XMLResource resource = (XMLResource) results.nextResource();
            System.out.println(resource.getContent());
        }
    }

    private static void listAverageAgePerBand() throws XMLDBException {
        Collection collection;
        collection = DatabaseManager.getCollection(Config.URI + Config.COLLECTION_PATH, Config.USER, Config.PASSWORD);
        XPathQueryService service = (XPathQueryService) collection.getService("XPathQueryService", "1.0");

        String query =
                """
                       xquery version "3.1";
                       let $currentYear := 2025
                       return
                         <Result>{
                           for $band in //Bands/Band
                           let $bandId := $band/@Id
                           let $bandName := $band/Name
                           let $musicians :=
                             for $bm in //BandMusicians/BandMusician[BandId = $bandId]
                             let $musician := //Musicians/Musician[@Id = $bm/MusicianId]
                             return $musician
                           let $ages := for $m in $musicians return $currentYear - xs:integer($m/BirthYear)
                           let $avgAge := round(avg($ages))
                           return
                             <Band averageAge="{ $avgAge }">{ $bandName/text() }</Band>
                         }</Result>
                """;

        var results = service.query(query).getIterator();
        while (results.hasMoreResources()) {
            XMLResource resource = (XMLResource) results.nextResource();
            System.out.println(resource.getContent());
        }
    }

    private static void listMusicians() throws XMLDBException {
        XMLResource res;
        Collection collection;
        collection = DatabaseManager.getCollection(Config.URI + Config.COLLECTION_PATH, Config.USER, Config.PASSWORD);
        res = (XMLResource) collection.getResource("Musicians.xml");
        if (res == null) {
            System.out.println("Document not found!");
        } else {
            System.out.println(res.getContent());
        }
    }

    public static void getMusicianByEmail(String email) throws XMLDBException {
        Collection collection;
        collection = DatabaseManager.getCollection(Config.URI + Config.COLLECTION_PATH, Config.USER, Config.PASSWORD);
        XPathQueryService service = (XPathQueryService) collection.getService("XPathQueryService", "1.0");

        String query = "/Musicians/Musician[Email='" + email + "']";
        var results = service.query(query).getIterator();

        if (results.hasMoreResources()) {
            XMLResource resource = (XMLResource) results.nextResource();
            System.out.println(resource.getContent());
        } else {
            System.out.println("NOT FOUND: " + email);
        }
    }

    private static void updateMusician(int musicianIdToEdit) throws XMLDBException {
        Collection collection;
        collection = DatabaseManager.getCollection(Config.URI + Config.COLLECTION_PATH, Config.USER, Config.PASSWORD);
        XPathQueryService service = (XPathQueryService) collection.getService("XPathQueryService", "1.0");

        // A "return replace value of" változat nem működik...
        String updateQuery =
                """
                        xquery version "3.1";
                        let $musician := collection('%s')/Musicians/Musician[@Id='%s']
                        return (
                            update replace $musician/Name with <Name>{concat(string($musician/Name), ' Edited')}</Name>,
                            update replace $musician/Email with <Email>{concat('edited.', string($musician/Email))}</Email>
                        )
                """.formatted(collection.getName(), musicianIdToEdit);

        service.query(updateQuery);

        System.out.println("OK: Name and email modified");
    }

    private static void deleteMusicianById(int musicianIdToDelete) throws XMLDBException {
        Collection collection;
        collection = DatabaseManager.getCollection(Config.URI + Config.COLLECTION_PATH, Config.USER, Config.PASSWORD);
        XPathQueryService service = (XPathQueryService) collection.getService("XPathQueryService", "1.0");


        String deleteQuery =
                """
                                xquery version "3.1";
                                let $musician := collection('%s')/Musicians/Musician[@Id='%s']
                                return update delete $musician
                """.formatted(collection.getName(), musicianIdToDelete);

        service.query(deleteQuery);

        System.out.println("OK: Musician deleted");
    }

    private static void initializeDriver() throws ClassNotFoundException, InstantiationException, IllegalAccessException, XMLDBException, NoSuchMethodException, InvocationTargetException {
        Class<?> cl = Class.forName(Config.DRIVER);
        Database database = (Database) cl.getDeclaredConstructor().newInstance();
        database.setProperty("create-database", "true");
        database.setProperty("user", Config.USER);
        database.setProperty("password", Config.PASSWORD);
        DatabaseManager.registerDatabase(database);
    }
}