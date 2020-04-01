CREATE TABLE "koodistoCache" (
    "id" SERIAL PRIMARY KEY,
    "updatedAt" TIMESTAMP
);


CREATE TABLE "cacheKey" (
    "id" SERIAL PRIMARY KEY,
    "koodistoCache" integer REFERENCES koodistoCache(id),
    "tyyppi" TEXT,
    "locale" TEXT,
    "parentUri" TEXT
);



CREATE TABLE "koodiItem" (
    "id" SERIAL PRIMARY KEY,
    "koodistoCache" integer REFERENCES koodistoCache(id),
    "koodistonTyyppi" TEXT,
    "koodiId" TEXT,
    "koodiUri" TEXT,
    "nimi" TEXT,
    "kuvaus" TEXT,
    "lyhytNimi" TEXT
);
