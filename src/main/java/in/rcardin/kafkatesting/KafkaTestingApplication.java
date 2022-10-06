package in.rcardin.kafkatesting;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@EnableKafka
@SpringBootApplication
public class KafkaTestingApplication {
	
	public static void main(String[] args) {
		SpringApplication.run(KafkaTestingApplication.class, args);
	}
	
	@Service
	static class MyKafkaProducer {
		
		private final KafkaTemplate<Integer, String> kafkaTemplate;
		
		@Autowired
		MyKafkaProducer(KafkaTemplate<Integer, String> kafkaTemplate) {
			this.kafkaTemplate = kafkaTemplate;
		}
		
		void send() {
			IntStream
					.range(0, 10)
					.forEach(i -> kafkaTemplate.send("test", i, String.valueOf(i)));
		}
	}
	
	@Component
	static class MyListener {
		
		private final AtomicInteger counter = new AtomicInteger();
		
		@KafkaListener(id = "listener", topics = "test")
		void listen(String message) {
			counter.incrementAndGet();
		}
		
		int getCounter() {
			return counter.get();
		}
	}
	
}
