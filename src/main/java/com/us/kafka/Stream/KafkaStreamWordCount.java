package com.us.kafka.Stream;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.KStreamBuilder;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;
import org.apache.kafka.streams.kstream.KeyValueMapper;
import org.apache.kafka.streams.kstream.ValueMapper;

import com.us.kafka.KafkaConfig;

import java.util.Arrays;
import java.util.Locale;
import java.util.Properties;

import static org.apache.kafka.common.serialization.Serdes.*;

/**
 * 
 * @ClassName KafkaStreamWordCount
 * @author abel
 * @date 2016年12月12日
 *  高层流DSL
 */
public class KafkaStreamWordCount {
	
	public static void main(String[] args) throws Exception {
        Properties props = new Properties();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, "streams-wordcount");
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG,  KafkaConfig.metadata_broker_list);
        props.put(StreamsConfig.ZOOKEEPER_CONNECT_CONFIG, KafkaConfig.zookeeper);
        props.put(StreamsConfig.KEY_SERDE_CLASS_CONFIG, String().getClass().getName());
        props.put(StreamsConfig.VALUE_SERDE_CLASS_CONFIG, String().getClass().getName());
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        KStreamBuilder builder = new KStreamBuilder();

        KStream<String, String> source = builder.stream(KafkaConfig.Producer_Topic);
        KTable<String, Long> counts = source
                .flatMapValues(new ValueMapper<String, Iterable<String>>() {
                    @Override
                    public Iterable<String> apply(String value) {
                        return Arrays.asList(value.toLowerCase(Locale.getDefault()).split(" "));
                    }
                }).map(new KeyValueMapper<String, String, KeyValue<String, String>>() {
                    public KeyValue<String, String> apply(String key, String value) {
                        return new KeyValue<String, String>(value, value+"--read");
                    }
                })
                .groupByKey().count("cunts");
//                .countByKey("Counts");
       
        counts.print();
//        counts.to(String(), Long(), "abel2");

        KafkaStreams streams = new KafkaStreams(builder, props);
        streams.start();
      
        Thread.sleep(3000);
//        streams.close();
    }
}
