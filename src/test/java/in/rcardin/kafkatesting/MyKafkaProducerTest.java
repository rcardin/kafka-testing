package in.rcardin.kafkatesting;

import in.rcardin.kafkatesting.KafkaTestingApplication.MyKafkaProducer;
import org.apache.kafka.clients.producer.MockProducer;
import org.apache.kafka.common.serialization.IntegerSerializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.kafka.core.KafkaTemplate;

class MyKafkaProducerTest {

  @Test
  void sendShouldSendMessagesToTopic() {
  
    try (MockProducer<Integer, String> mockProducer =
        new MockProducer<>(false, new IntegerSerializer(), new StringSerializer())) {
  
      final KafkaTemplate<Integer, String> mockedKafkaTemplate =
          new KafkaTemplate<>(() -> mockProducer);
  
      final MyKafkaProducer myKafkaProducer = new MyKafkaProducer(mockedKafkaTemplate);
      
      myKafkaProducer.send();
      
      Assertions.assertThat(mockProducer.history()).hasSize(10);
    }
  }
}
