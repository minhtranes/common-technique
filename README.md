# spring-boot-infinispan-in-memory-datastore
A Spring Boot sample to 
- Use Infinispan as a in-memory repository, and enable data distribution for HA
- USe RocksDB to achieve resiliency for the application

## Get Started
### Build the application
You must compile the maven project so that the protobuf schemas are generated 

```
mvn clean package
```

### Run multiple app instances to test the resiliency and high availability
To start the application instance, you must define the environment variable
- *PORT*: set the instance listing port
- *NODE_ID*: set the infinispan node id, its value must be one of ```node1``` or ```node2```

Start the app instances and verify by accessing to one of the two instance for api spec at ```/docs```