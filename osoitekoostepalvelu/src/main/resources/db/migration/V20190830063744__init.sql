CREATE TABLE "organisaatio" (
    "oid" SERIAL PRIMARY KEY,
    "version" BIGINT,
    "parentOid" TEXT,
    "parentOidPath" TEXT[],
    "tyypit" TEXT[],
    "kieletUris" TEXT[],
    "kotipaikkaUri"  TEXT,
    "koulutusluvat" TEXT[],
    "maaUri" TEXT,
    "oppilaitosTyyppiUri" TEXT,
    "oppilaitoskoodi" TEXT,
    "cachedAt" TIMESTAMP,
    "toimipistekoodi" TEXT,
    "lakkautusPvm" TIMESTAMP,
    "alkuPvm" TIMESTAMP,
    "vuosiluokat" TEXT[],
    "kriisitiedotuksenEmail" TEXT,
    "varhaiskasvatuksenYhteyshenkilo" TEXT,
    "varhaiskasvatuksenEmail" TEXT,
    "koskiYhdyshenkilo" TEXT,
    "yritysmuoto" TEXT,
    "ytunnus" TEXT
);

CREATE TABLE "organisaationimi" (
    "organisaatio" INTEGER PRIMARY KEY REFERENCES organisaatio(oid),
    "fi" TEXT,
    "sv" TEXT,
    "en" TEXT
);



CREATE TABLE "yhteystieto" (
    "id" SERIAL PRIMARY KEY,
    "organisaatio" INTEGER REFERENCES organisaatio(oid),
    "osoiteTyyppi" TEXT,
    "yhteystietoOid" TEXT,
    "kieli" TEXT,
    "osoite" TEXT,
    "postitoimipaikka" TEXT,
    "postinumeroUri" TEXT,
    "extraRivi" TEXT,
    "maaUri" TEXT,
    "email" TEXT,
    "numero" TEXT,
    "www" TEXT
);

CREATE TABLE "postiosoite" (
    "organisaatio" INTEGER PRIMARY KEY REFERENCES organisaatio(oid),
    "osoiteTyyppi" TEXT,
    "yhteystietoOid" TEXT,
    "postinumeroUri" TEXT,
    "osoite" TEXT,
    "postitoimipaikka" TEXT,
    "extraRivi" TEXT
);


CREATE TABLE "yhteystietoarvo" (
    "id" SERIAL PRIMARY KEY,
    "organisaatio" INTEGER REFERENCES organisaatio(oid),
    "elementtiTyyppi" TEXT,
    "elementtiOid" TEXT,
    "tyyppiNimi" TEXT,
    "tyyppiOid" TEXT,
    "kieli" TEXT,
    "arvo" TEXT
);

CREATE TABLE "koodistocache" (
    "id" SERIAL PRIMARY KEY,
    "updatedAt" TIMESTAMP
);


CREATE TABLE "cachekey" (
    "id" SERIAL PRIMARY KEY,
    "koodistocache" integer REFERENCES koodistocache(id),
    "tyyppi" TEXT,
    "locale" TEXT,
    "parentUri" TEXT
);



CREATE TABLE "koodiitem" (
    "id" SERIAL PRIMARY KEY,
    "koodistocache" integer REFERENCES koodistocache(id),
    "koodistonTyyppi" TEXT,
    "koodiId" TEXT,
    "koodiUri" TEXT,
    "nimi" TEXT,
    "kuvaus" TEXT,
    "lyhytNimi" TEXT
);

CREATE TABLE "savedsearch" (
    "id" SERIAL PRIMARY KEY,
    "name" TEXT,
    "ownerUserOid" TEXT,
    "searchType" TEXT,
    "addressFields" TEXT[],
    "createdAt" TIMESTAMP,
    "lang" TEXT
);



CREATE TABLE "targetgroup" (
    "id" SERIAL PRIMARY KEY,
    "savedsearch" integer REFERENCES savedsearch(id),
    "savedsearch_key" integer REFERENCES savedsearch(id),
    "type" TEXT,
    "options" TEXT[]
);


CREATE TABLE "searchterm" (
    "id" SERIAL PRIMARY KEY,
    "savedsearch" integer REFERENCES savedsearch(id),
    "savedsearch_key" integer REFERENCES savedsearch(id),
    "type" TEXT,
    "values" TEXT[]
);

CREATE TABLE "sequence" (
    "id" SERIAL PRIMARY KEY,
    "name" TEXT,
    "sequence" BIGINT
);