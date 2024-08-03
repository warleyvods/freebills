package com.freebills.configs;

import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.KafkaAdmin;

import java.util.HashMap;

@Configuration
public class TopicsConfig {

    public KafkaAdmin admin() {
        var configs = new HashMap<String, Object>();
       configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
       var admin = new KafkaAdmin(configs);
       admin.setFatalIfBrokerNotAvailable(true);
       return admin;
    }

    @Bean
    public NewTopic transactionsTopic() {
        return TopicBuilder
                .name("transactions")
                .partitions(1)
                .replicas(1)
                .config("enable.auto.commit", "false")
                .build();
    }
}
