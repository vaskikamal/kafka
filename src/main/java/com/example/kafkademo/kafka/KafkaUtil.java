package com.example.kafkademo.kafka;

import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

@Component
public class KafkaUtil<K,V> {
	
	private static final Logger logger = Logger.getLogger(KafkaUtil.class);
	
	
	public Producer<K, V> createKafkaProducer(KafkaCargo kafkaCargo) {
		Properties configProperties = new Properties();
        configProperties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaCargo.getKafkaClusterUrl());
        configProperties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, 
        		kafkaCargo.getKeySerializer());
        configProperties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, 
        		kafkaCargo.getValueSerializer());
        configProperties.put(ProducerConfig.RETRIES_CONFIG, kafkaCargo.PRODUCER_RETIRES);
        configProperties.put(ProducerConfig.BATCH_SIZE_CONFIG, kafkaCargo.DEFAULT_BATCH_SIZE);
        configProperties.put(ProducerConfig.ACKS_CONFIG, kafkaCargo.getProducerAckMode());
      
        if(kafkaCargo.getProducerClientId() != null)  {
        	 configProperties.put(ProducerConfig.CLIENT_ID_CONFIG, kafkaCargo.getProducerClientId());
        }
	    
        return new KafkaProducer<K, V> (configProperties);
	}
	
	public KafkaConsumer<K, V> createKafkaConsumer(KafkaCargo kafkaCargo) {
		Properties configProperties = new Properties();
        configProperties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaCargo.getKafkaClusterUrl());
        configProperties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, 
        		kafkaCargo.getKeyDeserializer());
        configProperties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
        		kafkaCargo.getValueDeserializer());
        configProperties.put(ConsumerConfig.GROUP_ID_CONFIG, kafkaCargo.getConsumerGroup());
        configProperties.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, kafkaCargo.ENABLE_AUTO_COMMIT);
        configProperties.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, kafkaCargo.AUTO_COMMIT_INTERVAL_MS);
        configProperties.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, kafkaCargo.SESSION_TIMEOUT_MS);
        configProperties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, kafkaCargo.AUTO_OFFSET_RESET_CONFIG);
        return new KafkaConsumer<K, V>(configProperties);	
	}
	
	public void publishMessage(Producer<K, V> producer, KafkaCargo kafkaCargo, K key, V value) {
		ProducerRecord<K,V> producerRecord = new ProducerRecord(kafkaCargo.getTopicName(), key, value);
		producer.send(producerRecord);
	}
}
