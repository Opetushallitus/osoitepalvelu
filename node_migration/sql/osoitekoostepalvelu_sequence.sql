CREATE TABLE "sequence" (
    "id" SERIAL PRIMARY KEY,
    "name" TEXT,
    "sequence" BIGINT,
);

INSERT INTO "sequence" ("id", "name", "sequence")
    VALUES
        (0, 'savedSearch', 3);

