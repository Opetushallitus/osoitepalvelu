const { Client } = require('pg');
const { MongoClient } = require('mongodb');
const sqlclient = new Client({
  user: 'app',
  host: 'localhost',
  database: 'osoitepalvelu',
  password: 'ophoph',
  port: 5432
});
const mongoUri = 'mongodb://localhost:27017';

const listDatabases = async (client) => {
  const dbs = client.db('osoite-db').admin();
  return dbs;
}

const main = async () => {

  try {
    // Connect to the MongoDB cluster
    const client = await MongoClient.connect(mongoUri, { useNewUrlParser: true });
    const db = client.db("osoite-db");
    const mongoDbNames = await listDatabases(client);
    console.log('mongoo', mongoDbNames);
    await client.close();
  } catch (e) {
    console.error(e);
  }
}

main().catch(e => console.error('Some error happened: ', e));
/*
const client = await MongoClient.connect(url, { useNewUrlParser: true })
  .catch(err => { console.log(err); });

if (!client) {
  return;
}

try {

  const db = client.db("testdb");

  let collection = db.collection('cars');

  let query = { name: 'Volkswagen' }

  let res = await collection.findOne(query);

  console.log(res);

} catch (err) {

  console.log(err);
} finally {

  client.close();
}

*/