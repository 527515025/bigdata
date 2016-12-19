package com.us.kafka;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Properties;
import java.util.concurrent.TimeUnit;

/**
 * Created by yangyibo on 16/12/14.
 */
public class KafkaProducerDemo extends Thread {
    private static String topicName;

    private KafkaProducerDemo(String topicName) {
        super();
        this.topicName = topicName;

    }

    @Override
    public void run() {

        Producer<String, String> producer = getKafkaProducer();
        for (int i = 0; i < 10000; i++) {
            producer.send(new ProducerRecord<>(topicName, "message: 我是KafkaProducerDemo第" + i + "条信息 abel"));
            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        producer.close();
    }

    public KafkaProducer<String, String> getKafkaProducer() {
        Properties props = new Properties();
        props.put("bootstrap.servers", KafkaConfig.metadata_broker_list);
        props.put("acks", "all");
        props.put("retries", 3);
        props.put("batch.size", 16384);
        props.put("linger.ms", 1);
        props.put("buffer.memory", 33554432);
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        return new KafkaProducer<>(props);
    }


    public static void creatKafkaProducer(String topicName) {
        new KafkaProducerDemo(topicName).start();// 使用kafka集群中创建好的主题 test
    }

}
