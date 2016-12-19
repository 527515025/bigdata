package com.us.kafka;

/**
 * 
 * @ClassName KafkaConfig
 * @author abel
 * @date 2016年11月8日
 */
public class KafkaConfig {
	public static final String zookeeper_local="192.168.100.25:2181";
	public static final String zookeeper_dev = "192.168.100.48:2181";
	public static final String zookeeper_qa = "192.168.100.77:2181,192.168.100.78:2181,192.168.100.79:2181";
	public static final String metadata_broker_list_local ="192.168.100.25:9092,192.168.100.25:9093,192.168.100.25:9094";
	public static final String metadata_broker_list_dev = "192.168.100.41:9092,192.168.100.42:9092,192.168.100.43:9092";
	public static final String metadata_broker_list_qa = "192.168.100.71:9092,192.168.100.72:9092,192.168.100.73:9092,192.168.100.74:9092,192.168.100.75:9092,192.168.100.76:9092";
	//public static final String metadata_broker_list_qa = "192.168.100.76:9092";

	public static final String metadata_broker_list =metadata_broker_list_qa;
	public static final String zookeeper=zookeeper_qa;

	public static final String Producer_Topic_dev = "abel2";
	public static final String Producer_Topic = "abelyang";
	public static final String Consumer__Topic = "abelyang";

	public static final String Consumer_groupId = "kafka-streaming-test";

	public static final String Consumer_zookeeper_session_timeout_ms = "40000";
	public static final String Consumer_zookeeper_sync_time_ms = "200";
	public static final String Consumer_auto_commit_interval_ms = "1000";


}
