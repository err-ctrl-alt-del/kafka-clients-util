package com.github.kafka.clients;

import org.apache.kafka.clients.producer.MockProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class KafkaClientsPythonHelperTest {

    private MockProducer<String, String> producer;

    @Before
    public void setUp() {
        producer = new MockProducer<>(
                true, new StringSerializer(), new StringSerializer());
    }

    @Test
    public void testSendSuccess() {
        Map<String, Object> kafkaParams = createKafkaParams();
        KafkaClientsPythonHelper helper = new KafkaClientsPythonHelper();
        helper.producer = producer;
        send(kafkaParams, helper, "0");
        List<ProducerRecord<String, String>> history = ((MockProducer) helper.producer).history();
        List<ProducerRecord<String, String>> expected = Arrays.asList(
                new ProducerRecord<>("topic_test", "0", "testValue"));
        Assert.assertEquals("Sent didn't match expected", expected, history);
    }

    private Map<String, Object> createKafkaParams() {
        Map<String, Object> kafkaParams = new HashMap<>();
        kafkaParams.put("bootstrap.servers", "localhost:9092");
        // kafkaParams.put("security.protocol", "SASL_PLAINTEXT");
        kafkaParams.put("acks", "all");
        kafkaParams.put("retries", 0);
        kafkaParams.put("batch.size", 16384);
        kafkaParams.put("linger.ms", 1);
        kafkaParams.put("buffer.memory", 33554432);
        kafkaParams.put("key.serializer",
                "org.apache.kafka.common.serialization.StringSerializer");
        kafkaParams.put("value.serializer",
                "org.apache.kafka.common.serialization.StringSerializer");
        return kafkaParams;
    }

    private void send(Map<String, Object> kafkaParams, KafkaClientsPythonHelper helper, String key) {
        try {
            helper.createProducer(kafkaParams);
            helper.send("topic_test", key, "testValue");
        } finally {
            helper.shutdownProducer();
        }
    }

}
