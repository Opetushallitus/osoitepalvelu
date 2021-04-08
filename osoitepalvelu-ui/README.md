Osoitepalvelu-UI
================

Osoitepalvelun hakukäytöliittymä. Toteutus AngularJS:llä.

Maven-kääntö minifioi CSS:t ja JavaScriptit. Käytä Maven versiota 3.

### Kehittäminen testiympäristöä vasten

Hakemistosta löytyy tarvittavat docker konfiguraatiotiedostot nginx välityspalvelimelle joka ohjaa pyynnöt taustajärjestelmiin halutuun
testiympäristöön (oletus untuva).

#### Käynnistys

`docker-compose up`

#### Autentikoituminen

Navigoi selaimella osoitteeseen http://localhost:8080/cas

Tunnistaudu testiympäristökohtaisilla tunnuksillasi

#### Osoitekoostepalvelun kehittäminen

Navigoi osoitteeseen http://localhost:8080

Nyt lähdekoodiin tehdyt muutokset näkyvät käyttöliittymässä aina kun uudelleenlataa sivun
