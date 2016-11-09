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
		KafkaProducer.creatProducer(KafkaConfig.Producer_Topic);
//		KafkaConsumer.creatConsumer(KafkaConfig.Consumer__Topic);
	}

}
