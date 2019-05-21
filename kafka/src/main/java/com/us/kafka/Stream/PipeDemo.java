package com.us.kafka.Stream;

import com.us.kafka.KafkaConfig;

import org.apache.kafka.streams.kstream.KStreamBuilder;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsConfig;
import static org.apache.kafka.common.serialization.Serdes.*;

import java.util.Properties;
/**
 * Created by yangyibo on 16/12/12.
 * 高层流DSL
 * 作为一个流管道
 */
public class PipeDemo {
    public static void main(String[] args) throws Exception {
        Properties props = new Properties();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, "streams-pipe");
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, KafkaConfig.metadata_broker_list);
        props.put(StreamsConfig.KEY_SERDE_CLASS_CONFIG, String().getClass());
        props.put(StreamsConfig.VALUE_SERDE_CLASS_CONFIG, String().getClass());

        // setting offset reset to earliest so that we can re-run the demo code with the same pre-loaded data
//        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        KStreamBuilder builder = new KStreamBuilder();
//        KStream<String ,String > kstream=builder.stream(KafkaConfig.Producer_Topic);
//        kstream.print();
//        kstream.to(KafkaConfig.Consumer__Topic);
        builder.stream(KafkaConfig.Producer_Topic).to(KafkaConfig.Consumer__Topic);

        KafkaStreams streams = new KafkaStreams(builder, props);
        streams.start();

        // usually the stream application would be running forever,
        // in this example we just let it run for some time and stop since the input data is finite.
        Thread.sleep(5000L);

//        streams.close();
    }
}
