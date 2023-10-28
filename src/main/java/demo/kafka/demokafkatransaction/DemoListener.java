package demo.kafka.demokafkatransaction;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class DemoListener {
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final DemoRepository demoRepository;

    @KafkaListener(id = "group1", topics = "topic1")
    @Transactional("transactionManager")
    //@Transactional("kafkaTransactionManager")
    public void listen1(String in) {
        log.info("Received from topic1: {}", in);

        log.info("Sending to topic2: {}", in.toUpperCase());
        kafkaTemplate.send("topic2", in.toUpperCase());

        log.info("Writing to database: {}", in);
        demoRepository.save(
                DemoEntity.builder()
                        .name(in)
                        .timestamp(System.currentTimeMillis())
                        .build()
        );
    }
}
