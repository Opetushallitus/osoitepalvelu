CREATE TABLE "savedSearch" (
    "id" SERIAL PRIMARY KEY,
    "name" TEXT,
    "ownerUserOid" TEXT,
    "searchType" TEXT,
    "addressFields" TEXT[],
    "createdAt" TIMESTAMP,
    "lang" TEXT
);



CREATE TABLE "targetGroup" (
    "id" SERIAL PRIMARY KEY,
    "savedSearch" integer REFERENCES savedSearch(id),
    "savedSearch_key" integer REFERENCES savedSearch(id),
    "type" TEXT,
    "options" TEXT[]
);


CREATE TABLE "searchTerm" (
    "id" SERIAL PRIMARY KEY,
    "savedSearch" integer REFERENCES savedSearch(id),
    "savedSearch_key" integer REFERENCES savedSearch(id),
    "type" TEXT,
    "values" TEXT[]
    PRIMARY KEY ("ID")
);