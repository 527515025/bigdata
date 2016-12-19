package com.us.kafka;

/**
 * 
 * @author yangyibo
 *
 */
public class Client {
	
	
	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		KafkaProducerDemo.creatKafkaProducer(KafkaConfig.Producer_Topic);
//		KafkaOldProducer.creatProducer(KafkaConfig.Producer_Topic);
//		KafkaConsumer.creatConsumer(KafkaConfig.Consumer__Topic);
	}

}
