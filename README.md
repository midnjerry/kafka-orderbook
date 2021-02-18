# spring-boot-kafka
Sample application with producer and consumer microservice that communicates by Kafka messaging

## Kafka Setup
https://kafka.apache.org/quickstart

Follow the instructions to run the `Zookeeper` and `Kafka Server` services.  These services must be running before you
can send and receive messages.

By following the instructions, you would have created a topic named `quickstart-events`.  We will use this topic for our
Spring Boot services.

## Consumer
1. Configure connection to Kafka using application.yml
2. Create @Service Consumer class and implement `consume` method.

## Producer
1. Configure connection to Kafka using application.yml
2. Create @Service Producer class that wraps KafkaTemplate class.  Send a message by calling `KafkaTemplate.send(...)`

```java
    private static final String TOPIC = "quickstart-events";

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    public void sendMessage(String message) {
        this.kafkaTemplate.send(TOPIC, message);
    }
```

### Endpoint (Producer Service)
POST http://localhost:8080/kafka/publish?message=Hello

or

POST http://localhost:8080/kafka/publish
form-data { "message": "Hello World" }

Output will appear in logs of `Consumer` microservice
