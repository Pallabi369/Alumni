const mongodb = require('mongodb');

let client;

module.exports = async function() {
  if (!client) {
      client = await mongodb.MongoClient.connect(process.env.CosmosDbConnectionString);
  }
  return client;
};
