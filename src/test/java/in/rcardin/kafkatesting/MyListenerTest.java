package in.rcardin.kafkatesting;

import in.rcardin.kafkatesting.MyListenerTest.TestConfiguration;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.MockConsumer;
import org.apache.kafka.clients.consumer.OffsetResetStrategy;
import org.apache.kafka.common.TopicPartition;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;

@SpringBootTest(classes = {TestConfiguration.class, KafkaTestingApplication.MyListener.class})
@EnableAutoConfiguration
class MyListenerTest {
  
  @Autowired private KafkaTestingApplication.MyListener myListener;

  @Test
  void listenShouldReceiveAllTheGivenMessages() {
    Assertions.assertThat(myListener.getCounter()).isEqualTo(3);
  }

  @Configuration
  static class TestConfiguration {

    @Bean
    KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<Integer, String>>
        kafkaListenerContainerFactory(ConsumerFactory<Integer, String> consumerFactory) {
      ConcurrentKafkaListenerContainerFactory<Integer, String> factory =
          new ConcurrentKafkaListenerContainerFactory<>();
      factory.setConsumerFactory(consumerFactory);
      factory.setConcurrency(1);
      factory.getContainerProperties().setPollTimeout(3000);
      return factory;
    }

    @Bean
    public ConsumerFactory<Integer, String> consumerFactory() {
      return new ConsumerFactory<>() {

        @Override
        public Map<String, Object> getConfigurationProperties() {
          return Map.of();
        }

        @Override
        public Consumer<Integer, String> createConsumer(
            String groupId, String clientIdPrefix, String clientIdSuffix) {
          final String topic = "test";
          final TopicPartition topicPartition = new TopicPartition(topic, 0);
          final MockConsumer<Integer, String> mockConsumer =
              new MockConsumer<>(OffsetResetStrategy.EARLIEST);
          final Map<TopicPartition, Long> beginningOffsets = new HashMap<>();
          beginningOffsets.put(topicPartition, 0L);
          mockConsumer.schedulePollTask(
              () -> {
                mockConsumer.rebalance(Collections.singletonList(topicPartition));
                mockConsumer.updateBeginningOffsets(beginningOffsets);
                mockConsumer.addRecord(new ConsumerRecord<>(topic, 0, 0, null, "foo"));
                mockConsumer.addRecord(new ConsumerRecord<>(topic, 0, 1, null, "bar"));
                mockConsumer.addRecord(new ConsumerRecord<>(topic, 0, 2, null, "baz"));
              });

          return mockConsumer;
        }

        @Override
        public boolean isAutoCommit() {
          return false;
        }
      };
    }
  }
}
