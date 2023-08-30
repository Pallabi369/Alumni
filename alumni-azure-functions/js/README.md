# Configuration

```
cat local.settings.json

{
  "IsEncrypted": false,
  "Values": {
    "FUNCTIONS_WORKER_RUNTIME": "node",
    "AzureWebJobsStorage": "UseDevelopmentStorage=true",
    "CosmosDbConnectionString": <connection_string>,
    "SsidProperty": "extension_ssid",
    "OptedInProperty": "extension_optedIn",
    "CosmosDbDatabase": "alumni",
    "CosmosDbCollection": "employees"
  }
}
```
