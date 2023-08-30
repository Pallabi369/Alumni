
# SAP API Mocks server

## Docker image

Make sure json schema files are copied over to the ``schema`` directory
```
npm run copy-schema
```

Execute the following command to build the image:
```
docker build . -t alumni/mocks-server:0.1.0 -f Dockerfile
```


### Run alumni/mocks-server image

```
docker run -d -p 3100:3100 alumni/mocks-server:0.1.0
```


### Example endpoints

```http request
http://apimocks:3100/api?type=P0001,P0002,P0006,P0008,P0009,P0014,P0015&user=510-12345678
```

```http request
http://apimocks:3100/clone?user=510-00460014
```

```http request
http://apimocks:3100/payroll-results?user=510-00460014
```
