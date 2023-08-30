
const connect = require('./db.js');

module.exports = async function (context, req) {

  const ssid = req.body[process.env.SsidProperty];
  if (!ssid) {
    const error = `No '${process.env.SsidProperty}' property found in the incoming request`;
    context.log(error);
    context.res = {
      status: 400,
      body: { error }
    }
    return;
  }

  const client = await connect();
  let db = client.db(process.env.CosmosDbDatabase);
  let zalarisIds = (await db.collection(process.env.CosmosDbCollection)
    .find({ssid}, {projection: {'_id': 1}}).toArray()).map(r => r._id)
  context.log(`Ssid: '${ssid}' is mapped to zalarisIds: [${zalarisIds}]`);

  context.res = {
    body: {
      version: "1.0.0",
      action: "Continue",
      [process.env.OptedInProperty]: zalarisIds.join(",")
    },
    status: 200,
    headers: {
      "Content-Type": "application/json"
    }
  };

}
