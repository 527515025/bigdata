package com.us.kafka;
import java.util.HashMap;  
import java.util.List;  
import java.util.Map;  
import java.util.Properties;

import kafka.consumer.Consumer;
import kafka.consumer.ConsumerConfig;  
import kafka.consumer.ConsumerIterator;  
import kafka.consumer.KafkaStream;  
import kafka.javaapi.consumer.ConsumerConnector;  

/**
 *   
 * @author yangyibo
 *
 */
public class KafkaConsumer extends Thread{
	private String topic;  
    
    public KafkaConsumer(String topic){  
        super();  
        this.topic = topic;  
    }  
      
      
    @Override  
    public void run() {  
        ConsumerConnector consumer = createConsumer();  
        Map<String, Integer> topicCountMap = new HashMap<String, Integer>();  
        topicCountMap.put(topic, 1); // 一次从主题中获取一个数据  
         Map<String, List<KafkaStream<byte[], byte[]>>>  messageStreams = consumer.createMessageStreams(topicCountMap);  
         KafkaStream<byte[], byte[]> stream = messageStreams.get(topic).get(0);// 获取每次接收到的这个数据  
         ConsumerIterator<byte[], byte[]> iterator =  stream.iterator();  
         while(iterator.hasNext()){  
             String message = new String(iterator.next().message());  
             System.out.println("接收到: " + message);  
         }  
    }  
  
    private ConsumerConnector createConsumer() {  
        Properties properties = new Properties();  
        properties.put("zookeeper.connect", KafkaConfig.zookeeper);//声明zk  
        properties.put("group.id", KafkaConfig.Consumer_groupId);// 必须要使用别的组名称， 如果生产者和消费者都在同一组，则不能访问同一组内的topic数据  
        properties.put("zookeeper.session.timeout.ms", KafkaConfig.Consumer_zookeeper_session_timeout_ms);
        properties.put("zookeeper.sync.time.ms", KafkaConfig.Consumer_zookeeper_sync_time_ms);
        properties.put("auto.commit.interval.ms", KafkaConfig.Consumer_auto_commit_interval_ms);
        return Consumer.createJavaConsumerConnector(new ConsumerConfig(properties));  
     }  
      
    /**
     * 
     * @param topicName
     */
    public static void creatConsumer(String topicName) {  
        new KafkaConsumer(topicName).start();// 使用kafka集群中创建好的主题 
    }   
}  