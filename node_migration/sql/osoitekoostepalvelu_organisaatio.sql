CREATE TABLE "organisaatio" (
    "oid" SERIAL PRIMARY KEY,
    "version" BIGINT,
    "parentOid" TEXT,
    "parentOidPath" TEXT[],
    "tyypit" TEXT[],
    "kieletUris" TEXT[],
    "kotipaikkaUri":  TEXT,
    "koulutusluvat" TEXT[],
    "maaUri": TEXT,
    "oppilaitosTyyppiUri" TEXT,
    "oppilaitoskoodi" TEXT,
    "cachedAt" TIMESTAMP,
    "toimipistekoodi" TEXT,
    "lakkautusPvm" TIMESTAMP,
    "alkuPvm" TIMESTAMP,
    "vuosiluokat" TEXT[],
    "kriisitiedotuksenEmail": TEXT,
    "varhaiskasvatuksenYhteyshenkilo" TEXT,
    "varhaiskasvatuksenEmail" TEXT,
    "koskiYhdyshenkilo" TEXT,
    "yritysmuoto" TEXT,
    "ytunnus" TEXT
);

CREATE TABLE "organisaatioNimi" (
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


CREATE TABLE "yhteystietoArvo" (
    "id" SERIAL PRIMARY KEY,
    "organisaatio" INTEGER PRIMARY KEY REFERENCES organisaatio(oid),
    "elementtiTyyppi" TEXT,
    "elementtiOid" TEXT,
    "tyyppiNimi" TEXT,
    "tyyppiOid" TEXT,
    "kieli" TEXT,
    "arvo" TEXT
);