
## SpringBatch <-> Terminated User mapping

Each request creates a JobInstance with parameters: {zalaris, requestTime} 

## Configuration

### Repeatable steps

API Request timeout configuration (*application.yml*) 

```
alumni:
  sync:
    batch:
      api:
       personaldata:
         url: http://apimock:3100/clone?user={zalarisId}
         timeout: 15s
       ...
```

Each step is repeatable and configured the following parameters (*RetryConfig*)

* interval: 30s
* backoff multiplier: 2x
* max-attempt: 3

On finishing attempts the step goes into **FAILED** status (along with job_execution).


## Performance tests

```
gradle :alumni-sync-app:gatlingRun-BasicSimulation
```
