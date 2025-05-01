# MongoDb CRUD

Az adatbázist egy `MongoDB Java Driver (Java Sync)` csomag segítsével érjük el és ennek segítségével végezzük el a szükséges feladatokat.

## Fejlesztés lépései:
#### 1. Új Maven projekt létrehozása
#### 2. Külső csomagok függőségeinek hozzáadása a pom.xml fájlban
```xml
<dependencies>
  <dependency>
    <groupId>org.mongodb</groupId>
    <artifactId>mongodb-driver-sync</artifactId>
    <version>5.4.0</version>
  </dependency>
  <dependency>
    <groupId>com.fasterxml.jackson.core</groupId>
    <artifactId>jackson-core</artifactId>
    <version>2.19.0</version>
  </dependency>
  <dependency>
    <groupId>com.fasterxml.jackson.core</groupId>
    <artifactId>jackson-databind</artifactId>
    <version>2.19.0</version>
  </dependency>
</dependencies>
```
> Az utolsó két függőségre csak azért volt szükség, hogy a Jackson könyvtár segítségével jelentősen egyszerűsíteni lehessen
a JSON fájlokkal történő munkát az adatok importálása során.

#### 3. Alkalmazás implementálása
A Main osztály működését két másik osztály támogatja:
- Config: Az alkalmazás konfigurációját tartalmazza (adatbázis elérés, xml forrásfájlok helye)
- DatabaseSeeder: Egy segéd osztály, amely minden egyes induláskor alapállapotba hozza az adatbázist. A megadott elérésen
  található XML fájlokat betölti az adatbázisba

#### 4. Az egyes CRUD műveletek implementálása

---

## Konzol eredménye

```declarative
========== Seed database with data ==========
 - Stored: Bands.json
 - Stored: Concerts.json
 - Stored: Musicians.json
 - Stored: Places.json

SUCCESS: All JSON files uploaded successfully!
=============================================



***** List all musicians *****
{"_id": 1, "Name": "John Doe", "Email": "john.doe@example.com", "BirthYear": 1973}
{"_id": 2, "Name": "Jane Smith", "Email": "jane.smith@example.com", "BirthYear": 1981}
{"_id": 3, "Name": "Mike Brown", "Email": "mike.brown@example.com", "BirthYear": 1989}
{"_id": 4, "Name": "Emily White", "Email": "emily.white@example.com", "BirthYear": 1970}
{"_id": 5, "Name": "Daniel Jones", "Email": "daniel.jones@example.com", "BirthYear": 1982}
{"_id": 6, "Name": "Sara Lee", "Email": "sara.lee@example.com", "BirthYear": 1979}
{"_id": 7, "Name": "Chris Green", "Email": "chris.green@example.com", "BirthYear": 1980}
{"_id": 8, "Name": "Laura King", "Email": "laura.king@example.com", "BirthYear": 1990}
{"_id": 9, "Name": "Robert Taylor", "Email": "robert.taylor@example.com", "BirthYear": 1991}
{"_id": 10, "Name": "Olivia Wilson", "Email": "olivia.wilson@example.com", "BirthYear": 1994}


***** Get musician by email: emily.white@example.com *****
{"_id": 4, "Name": "Emily White", "Email": "emily.white@example.com", "BirthYear": 1970}


***** List the members of each band *****
Band Name: The Rockers
Leader: John Doe
Members:John Doe, Jane Smith, Mike Brown

Band Name: Jazz Vibes
Leader: Jane Smith
Members:Emily White, Daniel Jones

Band Name: Blues Express
Leader: Mike Brown
Members:Sara Lee, Chris Green, Laura King, Robert Taylor, Olivia Wilson



***** List the musicians average age for each band *****
The Rockers: 44
Jazz Vibes: 49
Blues Express: 38


***** What is the name of the venue where the last concert will be held and what is the email address of the main band? *****
Venue: The Hard Rock Café
Main band email: john.doe@example.com


***** Update the name of musician with ID = 4 *****


***** Delete the musician with ID = 10 *****


***** List all musicians after modifications *****
{"_id": 1, "Name": "John Doe", "Email": "john.doe@example.com", "BirthYear": 1973}
{"_id": 2, "Name": "Jane Smith", "Email": "jane.smith@example.com", "BirthYear": 1981}
{"_id": 3, "Name": "Mike Brown", "Email": "mike.brown@example.com", "BirthYear": 1989}
{"_id": 4, "Name": "Emily White Edited", "Email": "editedemily.white@example.com", "BirthYear": 1970}
{"_id": 5, "Name": "Daniel Jones", "Email": "daniel.jones@example.com", "BirthYear": 1982}
{"_id": 6, "Name": "Sara Lee", "Email": "sara.lee@example.com", "BirthYear": 1979}
{"_id": 7, "Name": "Chris Green", "Email": "chris.green@example.com", "BirthYear": 1980}
{"_id": 8, "Name": "Laura King", "Email": "laura.king@example.com", "BirthYear": 1990}
{"_id": 9, "Name": "Robert Taylor", "Email": "robert.taylor@example.com", "BirthYear": 1991}
```
