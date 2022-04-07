osoitepalvelu
=============

## Technologies & Frameworks

Below is non-exhaustive list of the key technologies & frameworks used in the project.

### Backend

* Spring Framework
* Spring Security (CAS)
* Apache Camel
* Mongo DB
* Swagger

### Frontend

* Angular 1.3
* JQuery 1.10
* Bootstrap

# Development

* Java 11
* Maven 3

## Running backend locally

Implementation _assumes_ stuff from the environment.
Following files with appropriate content should be found from `~/oph-configuration` -directory.
Contents can be copied from one of the existing environments.
* security-context-backend.xml
* common.properties

After configurations are in place, run following commands at at project root: 
1. `mvn clean install`
2. `cd osoitekoostepalvelu`
3. `mvn jetty:run`

## Database

    docker run --name osoite-db -p 27017:27017 -d mongo:3.4
