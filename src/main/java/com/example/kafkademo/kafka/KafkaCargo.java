package com.example.kafkademo.kafka;

public class KafkaCargo {	
	
	
	final int PRODUCER_RETIRES = 0;
	final int DEFAULT_BATCH_SIZE = 16384;
	private static final String ACK_MODE_LEADER_ONLY= "1";
	
	final String ENABLE_AUTO_COMMIT="true";
	final String SESSION_TIMEOUT_MS="30000";
	final String AUTO_COMMIT_INTERVAL_MS = "1000";
	final String AUTO_OFFSET_RESET_CONFIG = "earliest";
	
	private String kafkaClusterUrl= "localhost:9092";
	private int noPartition = 1;
	private int replicationFactor = 1;
	private String topicName;
	private String producerAckMode = ACK_MODE_LEADER_ONLY;
	
	private String keySerializer = "org.apache.kafka.common.serialization.StringSerializer";
	private String valueSerializer = "org.apache.kafka.common.serialization.StringSerializer";
	
	private String keyDeserializer = "org.apache.kafka.common.serialization.StringDeserializer";
	private String valueDeserializer = "org.apache.kafka.common.serialization.StringDeserializer";
	
	private String consumerGroup;
	private String producerClientId;
	
	public String getKafkaClusterUrl() {
		return kafkaClusterUrl;
	}

	public void setKafkaClusterUrl(String kafkaClusterUrl) {
		this.kafkaClusterUrl = kafkaClusterUrl;
	}

	public String getProducerAckMode() {
		return producerAckMode == null ? ACK_MODE_LEADER_ONLY: producerAckMode;
	}

	public void setProducerAckMode(String producerAckMode) {
		this.producerAckMode = producerAckMode;
	}

	public int getNoPartition() {
		return noPartition;
	}

	public void setNoPartition(int noPartition) {
		this.noPartition = noPartition;
	}

	public int getReplicationFactor() {
		return replicationFactor;
	}

	public void setReplicationFactor(int replicationFactor) {
		this.replicationFactor = replicationFactor;
	}

	public String getTopicName() {
		return topicName;
	}

	public void setTopicName(String topicName) {
		this.topicName = topicName;
	}

	public String getKeySerializer() {
		return keySerializer;
	}

	public void setKeySerializer(String keySerializer) {
		this.keySerializer = keySerializer;
	}

	public String getValueSerializer() {
		return valueSerializer;
	}

	public void setValueSerializer(String valueSerializer) {
		this.valueSerializer = valueSerializer;
	}

	public String getKeyDeserializer() {
		return keyDeserializer;
	}

	public void setKeyDeserializer(String keyDeserializer) {
		this.keyDeserializer = keyDeserializer;
	}

	public String getValueDeserializer() {
		return valueDeserializer;
	}

	public void setValueDeserializer(String valueDeserializer) {
		this.valueDeserializer = valueDeserializer;
	}

	public String getConsumerGroup() {
		return consumerGroup;
	}

	public void setConsumerGroup(String consumerGroup) {
		this.consumerGroup = consumerGroup;
	}

	public String getProducerClientId() {
		return producerClientId;
	}

	public void setProducerClientId(String producerClientId) {
		this.producerClientId = producerClientId;
	}
	
	

}
