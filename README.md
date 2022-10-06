# Kafka Unit Testing using Spring Kafka and `MockProducer`/`MockConsumer` Classes

Inside the Kafka library, there are two mock implementation of the `Consumer<K, V>` and
`Producer<K, V>` interfaces. These classes are `MockConsumer` and `MockProducer`. These classes are
useful for unit testing Kafka consumers and producers. Please, refer to the official documentation
to understand how to use them in the wild:

* [How to build your first Apache KafkaProducer application](https://developer.confluent.io/tutorials/creating-first-apache-kafka-producer-application/confluent.html)
* [How to build your first Apache KafkaConsumer application](https://developer.confluent.io/tutorials/creating-first-apache-kafka-consumer-application/confluent.html)

The [Spring Kafka](https://docs.spring.io/spring-kafka/docs/current/reference/html/) project is a
great library to build client application using abstraction of the
Kafka consumers and producers managed by Spring. It abstracts the producer using a `KafkaTemplate<K, V>` type
, and the annotation `@KafkaListener` to consume messages from a topic.

Can we use the `MockConsumer` and `MockProducer` classes with Spring Kafka? The answer is yes, and
this is what this project is about.

Please, refer to the `src/test` folder to see how.

Is the use of the `MockConsumer` and `MockProducer` classes with Spring Kafka a good idea? Well, I
don't think so. In the case of the consumer, we can call the method annotated with `@KafkaListener` 
directly. Otherwise, in the case of the producer, we can use the `KafkaTemplate<K, V>` type to 
create a mock using Mockito, or some other mocking framework.

🤷‍♂️