package com.github.kafka.clients;


import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.KafkaException;
import java.util.Map;

public class KafkaClientsPythonHelper {

    protected Producer<String, String> producer;

    public void send(String topic, String key, String value) {
        validate(topic, value);
        producer.send(new ProducerRecord<>(topic, key, value));
    }

    private void validate(String topic, String value) {
        if (topic == null || topic.isEmpty()) {
            throw new IllegalArgumentException("Topic cannot be null or empty.");
        }
        if (value == null || value.isEmpty()) {
            throw new IllegalArgumentException("Value cannot be null or empty.");
        }
    }

    public void createProducer(Map<String, Object> kafkaParams) throws IllegalArgumentException {
        if (kafkaParams == null || kafkaParams.isEmpty()) {
            throw new IllegalArgumentException("Kafka Parameters should always be non-empty or null.");
        }
        try {
            if (producer == null) {
                producer = new KafkaProducer<>(kafkaParams);
            }
        } catch (KafkaException e) {
            throw new KafkaException("Error creating KafkaProducer", e);
        }
    }

    public void flushProducer() {
        if (producer != null) {
            producer.flush();
        }
    }

    public void shutdownProducer() {
        if (producer != null) {
            producer.flush();
            producer.close();
        }
    }

}