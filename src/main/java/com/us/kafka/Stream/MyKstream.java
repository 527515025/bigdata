package com.us.kafka.Stream;

import java.util.Arrays;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KStreamBuilder;
import org.apache.kafka.streams.kstream.KTable;
import org.apache.kafka.streams.kstream.KeyValueMapper;
import org.apache.kafka.streams.kstream.Predicate;
import org.apache.kafka.streams.kstream.ValueMapper;
import com.us.kafka.KafkaConfig;
import java.util.Properties;
import static org.apache.kafka.common.serialization.Serdes.String;

/**
 * Created by yangyibo on 16/12/12. my learn demo
 * 高层流DSL
 */
public class MyKstream {
    public static void main(String[] args) {

        KStreamBuilder builder = new KStreamBuilder();
//        filterWordCount(builder);
        lambdaFilter(builder);
        KafkaStreams ks = new KafkaStreams(builder, init());
        ks.start();
//        Runtime.getRuntime().addShutdownHook(new Thread(ks::close));
    }

    public static Properties init() {
        Properties properties = new Properties();
        properties.setProperty(StreamsConfig.APPLICATION_ID_CONFIG, "MyKstream2");
        properties.setProperty(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, KafkaConfig.metadata_broker_list);
        properties.setProperty(StreamsConfig.ZOOKEEPER_CONNECT_CONFIG, KafkaConfig.zookeeper);
        properties.put(StreamsConfig.KEY_SERDE_CLASS_CONFIG, String().getClass().getName());
        properties.put(StreamsConfig.VALUE_SERDE_CLASS_CONFIG, String().getClass().getName());
        properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        return properties;
    }


    private static void filterWordCount(KStreamBuilder builder) {
        KStream<String, String> source = builder.stream(KafkaConfig.Producer_Topic);
        KTable<String, Long> count = source.flatMapValues(new ValueMapper<String, Iterable<String>>() {
            @Override
            public Iterable<String> apply(String value) {
                return Arrays.asList(value.split(" "));
            }
        }).filter(new Predicate<String, String>() {

            @Override
            public boolean test(String key, String value) {
                if (value.contains("abel")) {
                    return true;
                }
                return false;
            }
        }).map(new KeyValueMapper<String, String, KeyValue<String, String>>() {

            public KeyValue<String, String> apply(String key, String value) {

                return new KeyValue<String, String>(value + "--read", value);
            }

        }).countByKey("us");
        count.print();
//        count.to(KafkaConfig.Consumer__Topic);
    }

    private static void lambdaFilter(KStreamBuilder builder) {
        KStream<String, String> textLines = builder.stream(KafkaConfig.Producer_Topic);
//		Pattern pattern = Pattern.compile("\\W+", Pattern.UNICODE_CHARACTER_CLASS);
                textLines
                .flatMapValues(value -> Arrays.asList(value.split(" ")))
                .map((key, word) -> new KeyValue<>(word, word))
                .filter((k,v)->(!k.contains("message")))
//				.through("RekeyedIntermediateTopic")
                .countByKey("Counts")
                .toStream().print();
    }


}
