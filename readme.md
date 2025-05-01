# Adatbázisrendszerek: CRUD alkalmazások készítése

> ## További fejezetek:
> 1. [Az adatmodell ismertetése](./data-model.md)
> 2. [eXist-db feladat ismertetése](./exist-db-crud.md)
> 3. [MongoDb feladat ismertetése](./mongo-db-crud.md)

Az alkalmazásokat Windows 11-es környezetben készítettem el. Az egyszerűség kedvéért az adatbázis szervereket docker-ben
telepítettem. A két alkalmazás ugyan azokat az adatmodelleket használja, természetesen némi hozzáigazítással a tényleges
adatbázis kezelőhöz. Mindkét alkalmazás hasonló funkciókat valósít meg:
- Adatok feltöltése (seed) az adatbázisba
- Egyszerű műveletek CRUD-ra
- Pár összetettebb lekérdezés 

## Kiszolgáló telepítés
### Docker
Windows környezetben a legkényelmesebb megoldás a `Docker Desktop for Windows` telepítése.

[Online segítség a telepítéshez](https://docs.docker.com/desktop/setup/install/windows-install/)

### eXist-db
```powershell
docker pull existdb/existdb:latest
docker run -it -d -p 8080:8080 -p 8443:8443 --name exist existdb/existdb:latest
```
[Online segítség a telepítéshez](https://exist-db.org/exist/apps/doc/docker)

### MongoDb
```powershell
docker pull mongodb/mongodb-community-server:latest
docker run --name mongodb -p 27017:27017 -d mongodb/mongodb-community-server:latest
```
[Online segítség a telepítéshez](https://www.mongodb.com/docs/manual/tutorial/install-mongodb-community-with-docker/)
