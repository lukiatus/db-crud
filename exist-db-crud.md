# eXist-db CRUD

Az adatbázist egy `XML:DB (Initiative for XML Databases)` csomag segítsével érjük el és ennek segítségével végezzük el a szükséges feladatokat.
Ez lényegében csak egy interfész leíró, amihez szükség van a tényleges megvalósításra. Erre az `EXist DB Core`csomagot
használtam fel.

## Fejlesztés lépései:
#### 1. Új Maven projekt létrehozása
#### 2. Külső csomagok függőségeinek hozzáadása a pom.xml fájlban
> A legújabb xmldb-api verzióval sajnos nem sikerült megoldanom a feladatot, ezért egy kicsit régebbi verziót használtam fel. 
```xml
<dependencies>
    <dependency>
        <groupId>net.sf.xmldb-org</groupId>
        <artifactId>xmldb-api</artifactId>
        <version>1.7.0</version>
    </dependency>
    <dependency>
        <groupId>org.exist-db</groupId>
        <artifactId>exist-core</artifactId>
        <version>6.3.0</version>
    </dependency>
</dependencies>
```
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
 - Stored: BandMusicians.xml
 - Stored: Bands.xml
 - Stored: ConcertBands.xml
 - Stored: Concerts.xml
 - Stored: Musicians.xml
 - Stored: Places.xml

SUCCESS: All XML files uploaded successfully!
=============================================



***** List all musicians *****
<Musicians>
  <Musician>
    <Email>john.doe@example.com</Email>
    <BirthYear>1973</BirthYear>
    <Id>1</Id>
    <Name>John Doe</Name>
  </Musician>
  <Musician>
    <Email>jane.smith@example.com</Email>
    <BirthYear>1981</BirthYear>
    <Id>2</Id>
    <Name>Jane Smith</Name>
  </Musician>
  <Musician>
    <Email>mike.brown@example.com</Email>
    <BirthYear>1989</BirthYear>
    <Id>3</Id>
    <Name>Mike Brown</Name>
  </Musician>
  <Musician>
    <Email>emily.white@example.com</Email>
    <BirthYear>1970</BirthYear>
    <Id>4</Id>
    <Name>Emily White</Name>
  </Musician>
  <Musician>
    <Email>daniel.jones@example.com</Email>
    <BirthYear>1982</BirthYear>
    <Id>5</Id>
    <Name>Daniel Jones</Name>
  </Musician>
  <Musician>
    <Email>sara.lee@example.com</Email>
    <BirthYear>1979</BirthYear>
    <Id>6</Id>
    <Name>Sara Lee</Name>
  </Musician>
  <Musician>
    <Email>chris.green@example.com</Email>
    <BirthYear>1980</BirthYear>
    <Id>7</Id>
    <Name>Chris Green</Name>
  </Musician>
  <Musician>
    <Email>laura.king@example.com</Email>
    <BirthYear>1990</BirthYear>
    <Id>8</Id>
    <Name>Laura King</Name>
  </Musician>
  <Musician>
    <Email>robert.taylor@example.com</Email>
    <BirthYear>1991</BirthYear>
    <Id>9</Id>
    <Name>Robert Taylor</Name>
  </Musician>
  <Musician>
    <Email>olivia.wilson@example.com</Email>
    <BirthYear>1994</BirthYear>
    <Id>10</Id>
    <Name>Olivia Wilson</Name>
  </Musician>
</Musicians>


***** Get musician by email: emily.white@example.com *****
<Musician>
    <Email>emily.white@example.com</Email>
    <BirthYear>1970</BirthYear>
    <Id>4</Id>
    <Name>Emily White</Name>
  </Musician>


***** List the members of each band *****
<Result><Band><Name>The Rockers</Name><Members>John Doe, Jane Smith, Mike Brown</Members></Band><Band><Name>Jazz Vibes</Name><Members>Emily White, Daniel Jones</Members></Band><Band><Name>Blues Express</Name><Members>Laura King, Sara Lee, Chris Green, Robert Taylor, Olivia Wilson</Members></Band></Result>


***** List the musicians average age for each band *****
<Result><Band averageAge="44">The Rockers</Band><Band averageAge="49">Jazz Vibes</Band><Band averageAge="38">Blues Express</Band></Result>


***** What is the name of the venue where the last concert will be held and what is the email address of the main band? *****
<Result><Venue><Name>The Hard Rock Café</Name></Venue><MainBandEmail><Email>john.doe@example.com</Email></MainBandEmail></Result>


***** Update the name of musician with ID = 4 *****
OK: Name and email modified


***** Delete the musician with ID = 10 *****
OK: Musician deleted


***** List all musicians after modifications *****
<Musicians>
  <Musician>
    <Email>john.doe@example.com</Email>
    <BirthYear>1973</BirthYear>
    <Id>1</Id>
    <Name>John Doe</Name>
  </Musician>
  <Musician>
    <Email>jane.smith@example.com</Email>
    <BirthYear>1981</BirthYear>
    <Id>2</Id>
    <Name>Jane Smith</Name>
  </Musician>
  <Musician>
    <Email>mike.brown@example.com</Email>
    <BirthYear>1989</BirthYear>
    <Id>3</Id>
    <Name>Mike Brown</Name>
  </Musician>
  <Musician>
    <Email>edited.emily.white@example.com</Email>
    <BirthYear>1970</BirthYear>
    <Id>4</Id>
    <Name>Emily White Edited</Name>
  </Musician>
  <Musician>
    <Email>daniel.jones@example.com</Email>
    <BirthYear>1982</BirthYear>
    <Id>5</Id>
    <Name>Daniel Jones</Name>
  </Musician>
  <Musician>
    <Email>sara.lee@example.com</Email>
    <BirthYear>1979</BirthYear>
    <Id>6</Id>
    <Name>Sara Lee</Name>
  </Musician>
  <Musician>
    <Email>chris.green@example.com</Email>
    <BirthYear>1980</BirthYear>
    <Id>7</Id>
    <Name>Chris Green</Name>
  </Musician>
  <Musician>
    <Email>laura.king@example.com</Email>
    <BirthYear>1990</BirthYear>
    <Id>8</Id>
    <Name>Laura King</Name>
  </Musician>
  <Musician>
    <Email>robert.taylor@example.com</Email>
    <BirthYear>1991</BirthYear>
    <Id>9</Id>
    <Name>Robert Taylor</Name>
  </Musician>
  
</Musicians>
```