package com.us.kafka;
import java.util.Properties;
import java.util.concurrent.TimeUnit;  
  
import kafka.javaapi.producer.Producer;  
import kafka.producer.KeyedMessage;  
import kafka.producer.ProducerConfig;  
import kafka.serializer.StringEncoder;  
 
/**
 * 
 * @author yangyibo
 *
 */
public class KafkaProducer extends Thread{
	 private String topic;  
     
	    public KafkaProducer(String topic){  
	        super();  
	        this.topic = topic;  
	    }  
	      
	      
	    @Override  
	    public void run() {  
	        Producer producer = createProducer();  
	        int i=0;  
	        while(i<=i+10000){  
	            producer.send(new KeyedMessage<Integer, String>(topic, "message: 我是第" + i+++"条信息"));  
	            try {  
	                TimeUnit.SECONDS.sleep(1);  
	            } catch (InterruptedException e) {  
	                e.printStackTrace();  
	            }  
	        }  
	    }  
	  
		private Producer createProducer() {  
	        Properties properties = new Properties();  
	        properties.put("zookeeper.connect", KafkaConfig.zookeeper);//声明zk  
	        properties.put("serializer.class", StringEncoder.class.getName());  
	        properties.put("metadata.broker.list", KafkaConfig.metadata_broker_list);// 声明kafka broker  
	        return new Producer<Integer, String>(new ProducerConfig(properties));  
	     }  
	      
	      
		/**
		 * 
		 * @param topicName
		 */
	    public static void creatProducer(String topicName) {  
	        new KafkaProducer(topicName).start();// 使用kafka集群中创建好的主题 test   
	    }  
	       
	} 