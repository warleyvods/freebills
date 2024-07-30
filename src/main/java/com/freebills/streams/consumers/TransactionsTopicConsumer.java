package com.freebills.streams.consumers;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.listener.AcknowledgingMessageListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;

@Component
public class TransactionsTopicConsumer implements AcknowledgingMessageListener<String, String> {

    private static final Logger log = LoggerFactory.getLogger(TransactionsTopicConsumer.class);

    // TODO: Make this idempotent so we don't end up processing a transaction twice (how)?
    // TODO: Make a secondary method that's capable of triggering a recount of all transactions for a consumer synchronously
    @Override
    @KafkaListener(id = "transactions-listener", topics = "transactions", clientIdPrefix = "freebills")
    public void onMessage(ConsumerRecord<String, String> data, Acknowledgment acknowledgment) {
        try {
            log.info("received: {}", data.value());
            acknowledgment.nack(Duration.of(1, ChronoUnit.SECONDS));
        } catch (Throwable t) {
            log.info("shiet: {}", data.value());
        }
    }
}

