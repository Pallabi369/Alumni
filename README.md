[![Build Status](https://dev.azure.com/zalaris/ZalarisLabs/_apis/build/status/alumni?branchName=main)](https://dev.azure.com/zalaris/ZalarisLabs/_build/latest?definitionId=45&branchName=main)

# Introduction 
 

# Getting Started

Please note that the project is in the phase of rapid development, therefore not all information may be up-to-date.

## Configuration

### Docker 

```
cp docker.env.sample docker.env
```

### ServiceBus configuration

```
alumni:
  sync:
    servicebus:
      connection-string: <Endpoint>
      queue-name: syncreq
```

## Development preparation

## Code styles

Before you modify any source file please do import of teh CodeStyle config from ``docs/CodeStyles.xml``.
https://www.jetbrains.com/help/idea/configuring-code-style.html#import-code-style

## XSD -> JSON schema conversion

1. Correct the XSD schema by adding the xsd:element above xsd:complex. The corrected document should look like:

```xsd
  <xsd:element name="DT_P0001">
    <xsd:complexType>
        <xsd:sequence>
```

2. ``npm install -g jgexml``
3. Convert document ``xsd2json.cmd DT_P0001.xsd DT_P0001.schema``
4. Correct the document by flattening the properties:

```
{
  "title": "DT_P0001.xsd",
  "$schema": "http://json-schema.org/schema#",
  "id": "http://zalaris.com/api/nonSAP/mdUpdateDirect",
  "type": "object",
  "properties": {
        "PERNR": {
          "type": "string",
          "description": "Personnel Number"
        },
        ...
```

## Hints

An example how to update ssid property via GraphExplorer:

```
POST https://graph.microsoft.com/v1.0/users/<user_uuid>$select=extension_<application_id>_ssid

{
"extension_<application_id>_ssid": ""
}
```

## Spring profiles

* **_dev_** - only for development purposes. Amongst other, it initializes spring-batch schema if none exists.
* **_inmemory_** - only for test/development purposes. All data is stored in volatile memory.
* **_mockbus_** - this profile is required if you don't connect your app to the *azure servicebus* service. 
It creates direct connection between the restapi client and the processing engine (no intermediate queue-based component)

# docker-compose setup

1. Follow instructions from sap-api-mocks/README.md to create apimock image
2. Build app images ``gradlew bootBuildImage``
3. Run docker-compose
```
docker-compose up --no-start # create containers
docker-compose start
```

# Zalaris SAP API mock

Alumni uses ```https://www.mocks-server.org/``` to simulate Zalaris SAP API.

The returned data is generated automatically based on the delivered XSD schema (converted to JSON schema) and data faker 
(```https://github.com/json-schema-faker```) so it doesn't make much sense, but it obeys the schema nonetheless.

Execute the following instruction to setup API mock.
```
cd sap-api-mocks;
npm install
npm run mocks
```

Then, point your browser to: http://localhost:3100/api?type=DT_P0006&id=123

Query parameters:
```
type - available schemas ('DT_P0002', 'DT_P0006', 'DT_P0008', 'DT_P0009', 'DT_P0014', 'DT_P0015', 
'MT_mdUpdateDirectRequest')
id - not used at the moment.
```

# Authorization 

## Access token request

```
curl --location --request POST 'https://login.microsoftonline.com/<<tenant_id>>/oauth2/v2.0/token'
--header 'Content-Type: application/x-www-form-urlencoded'
--data-urlencode 'client_secret=<<client_secret>>'
--data-urlencode 'grant_type=client_credentials'
--data-urlencode 'client_id=<<client_id>>'
--data-urlencode 'scope=api://<<client_id>>/.default'
```

# Build and Test
