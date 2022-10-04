package in.rcardin.kafkatesting;

import java.util.stream.IntStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.KafkaTemplate;
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
	
}
