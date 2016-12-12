package com.us.kafka;
/**
 * 
 * @ClassName KafkaConfig
 * @author abel
 * @date 2016年11月8日
 */
public class KafkaConfig {
	public static final String zookeeper = "192.168.100.48:2181";
	public static final String metadata_broker_list = "192.168.100.41:9092,192.168.100.42:9092,192.168.100.43:9092";
	public static final String Producer_Topic = "abel";
	public static final String Consumer__Topic = "abel2";

	public static final String Consumer_groupId = "spark-streaming-test";
	public static final String Consumer_zookeeper_session_timeout_ms = "40000";
	public static final String Consumer_zookeeper_sync_time_ms = "200";
	public static final String Consumer_auto_commit_interval_ms = "1000";

}
