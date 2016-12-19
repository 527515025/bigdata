package com.us.kafka.Stream;
import com.us.kafka.KafkaConfig;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.processor.Processor;
import org.apache.kafka.streams.processor.ProcessorContext;
import org.apache.kafka.streams.processor.ProcessorSupplier;
import org.apache.kafka.streams.processor.TopologyBuilder;
import org.apache.kafka.streams.state.KeyValueIterator;
import org.apache.kafka.streams.state.KeyValueStore;
import org.apache.kafka.streams.state.Stores;

import java.util.Locale;
import java.util.Properties;

import static org.apache.kafka.common.serialization.Serdes.*;

/**
 * Created by yangyibo on 16/12/12.
 */
public class WordCountProcessorDemo {
    private static class MyProcessorSupplier implements ProcessorSupplier<String, String> {

        @Override
        public Processor<String, String> get() {
            return new Processor<String, String>() {
                private ProcessorContext context;
                private KeyValueStore<String, Integer> kvStore;

                @Override
                @SuppressWarnings("unchecked")
                public void init(ProcessorContext context) {
                    this.context = context;
                    this.context.schedule(1000);
                    this.kvStore = (KeyValueStore<String, Integer>) context.getStateStore("Counts");
                }

                @Override
                public void process(String dummy, String line) {
                    String[] words = line.toLowerCase(Locale.getDefault()).split(" ");

                    for (String word : words) {
                        Integer oldValue = this.kvStore.get(word);
                        System.out.println(words);
                        if (oldValue == null) {
                            this.kvStore.put(word, 1);
                        } else {
                            this.kvStore.put(word, oldValue + 1);
                        }
                    }

                    context.commit();
                }

                @Override
                public void punctuate(long timestamp) {
                    try (KeyValueIterator<String, Integer> iter = this.kvStore.all()) {
                        System.out.println("----------- " + timestamp + " ----------- ");

                        while (iter.hasNext()) {
                            KeyValue<String, Integer> entry = iter.next();

                            System.out.println("[" + entry.key + ", " + entry.value + "]");

                            context.forward(entry.key, entry.value.toString());
                        }
                    }
                }

                @Override
                public void close() {
                    this.kvStore.close();
                }
            };
        }
    }

    public static void main(String[] args) throws Exception {
        Properties props = new Properties();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, "streams-wordcount-processor");
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, KafkaConfig.metadata_broker_list);
        props.put(StreamsConfig.ZOOKEEPER_CONNECT_CONFIG, KafkaConfig.zookeeper);
        props.put(StreamsConfig.KEY_SERDE_CLASS_CONFIG, String().getClass());
        props.put(StreamsConfig.VALUE_SERDE_CLASS_CONFIG, String().getClass());

        // setting offset reset to earliest so that we can re-run the demo code with the same pre-loaded data
//        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        TopologyBuilder builder = new TopologyBuilder();

        builder.addSource("Source", KafkaConfig.Producer_Topic);

        builder.addProcessor("Process", new MyProcessorSupplier(), "Source");
        builder.addStateStore(Stores.create("Counts").withStringKeys().withIntegerValues().inMemory().build(), "Process");

//        builder.addSink("Sink", KafkaConfig.Consumer__Topic, "Process");

        KafkaStreams streams = new KafkaStreams(builder, props);
        streams.start();

        // usually the stream application would be running forever,
        // in this example we just let it run for some time and stop since the input data is finite.
        Thread.sleep(5000L);

//        streams.close();
    }
}
