package demo.kafka.demokafkatransaction;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.transaction.KafkaTransactionManager;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;

@Slf4j
@SpringBootApplication
public class DemoKafkaTransactionApplication {
    public static void main(String[] args) {
        SpringApplication.run(DemoKafkaTransactionApplication.class, args);
    }

    @Bean
    public ApplicationRunner transactionManagersId(
            PlatformTransactionManager transactionManager,
            KafkaTransactionManager<String, String> kafkaTransactionManager
    ) {
        return args -> {
            log.info("Transaction manager: {}", transactionManager.getClass().getName());
            log.info("Kafka transaction manager: {}", kafkaTransactionManager.getClass().getName());
        };
    }

    @Bean
    public ApplicationRunner sendKafkaMessage(KafkaTemplate<String, String> template) {
        return args -> template.executeInTransaction(t -> t.send("topic1", "test"));
    }

    @Bean
    public NewTopic topic1() {
        return TopicBuilder.name("topic1").build();
    }

    @Bean
    public NewTopic topic2() {
        return TopicBuilder.name("topic2").build();
    }
}
