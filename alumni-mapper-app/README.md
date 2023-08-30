
### OpenAPI 

http://{server:port}/v3/api-docs


### CosmosDB gotcha: 
FindEmploymentHistoryMongoAccess.findBySsid() makes a sort by "p0001.endda" property.
CosmosDB requires index omn fields that are used as sort props, error:
``The index path corresponding to the specified order-by item is excluded.``

Solution:
https://docs.microsoft.com/en-us/answers/questions/258032/the-index-path-corresponding-to-the-specified-orde.html
https://docs.microsoft.com/en-us/azure/cosmos-db/mongodb/mongodb-indexing#indexing-for-mongodb-server-version-36
