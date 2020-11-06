package micro;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.containers.PostgreSQLContainer;

@Profile("integration-test")
@Configuration
class IntegrationConfiguration {

    static {
        var kafkaContainer = new KafkaContainer()
                .withReuse(true)
                .withNetwork(null);
        kafkaContainer.start();
        System.setProperty("spring.kafka.bootstrap-servers", kafkaContainer.getBootstrapServers());

        var postgreSQLContainer = new PostgreSQLContainer<>()
                .withReuse(true);
        postgreSQLContainer.start();
        System.setProperty("spring.datasource.url", postgreSQLContainer.getJdbcUrl());
        System.setProperty("spring.datasource.username", postgreSQLContainer.getUsername());
        System.setProperty("spring.datasource.password", postgreSQLContainer.getPassword());
    }

    @Bean
    NewTopic createLocationUpdatesTopic() {
        return new NewTopic("location-updates", 1, (short) 1);
    }
}
