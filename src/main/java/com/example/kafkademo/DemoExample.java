package com.example.kafkademo;

import java.util.ArrayList;
import java.util.List;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.kafkademo.kafka.KafkaCargo;
import com.example.kafkademo.kafka.KafkaUtil;
import com.example.kafkademo.kafka.ZookeeperKafkaUtil;

@Service
public class DemoExample {
	
	private static final Logger logger = Logger.getLogger(DemoExample.class);
	@Autowired
	private ZookeeperKafkaUtil  zookeeperKafkaUtil;
	@Autowired
	private KafkaUtil<String, String> kafkaUtil;
	
	public void performDemo() {
		KafkaCargo cargo = new KafkaCargo();
		cargo.setTopicName("kafkademo");
		cargo.setProducerClientId("myProducer");
		cargo.setConsumerGroup("Myconsumergroup");
		 
		boolean topicExist = zookeeperKafkaUtil.isTopicExists("test");
		logger.info("Test topic exist: "+topicExist);
		
		if(!zookeeperKafkaUtil.isTopicExists(cargo.getTopicName())) {
			zookeeperKafkaUtil.createKafkaTopic(cargo.getTopicName(), 
					cargo.getReplicationFactor(), cargo.getNoPartition());
		}
			
		ConsumerThread consumerRunnable = new ConsumerThread(cargo);
        consumerRunnable.start();
        try {
			Thread.sleep(1000);
			consumerRunnable.getKafkaConsumer().wakeup();
			consumerRunnable.join();
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}       
		Producer<String, String> producer = kafkaUtil.createKafkaProducer(cargo);
		kafkaUtil.publishMessage(producer, cargo, null, "Value 118");
		kafkaUtil.publishMessage(producer, cargo, null, "Value 19");
		kafkaUtil.publishMessage(producer, cargo, null, "Value 10");
		
		producer.close();
	}
	
	private class ConsumerThread extends Thread {
		private KafkaCargo cargo;
		private KafkaConsumer<String, String> kafkaConsumer = null;
		
		public ConsumerThread(KafkaCargo cargo){
			this.cargo = cargo;
		}
		
		public KafkaConsumer<String, String> getKafkaConsumer() {
			return this.kafkaConsumer;
		}

		public void run() {			
			try {
				this.kafkaConsumer = kafkaUtil.createKafkaConsumer(cargo);
				List<String> topics = new ArrayList<String>();
				topics.add(cargo.getTopicName());
				kafkaConsumer.subscribe(topics);
				
                while (true) {
                    ConsumerRecords<String, String> records = kafkaConsumer.poll(100);
                    for (ConsumerRecord<String, String> record : records)
                        logger.info(record.value());
                }
            }catch(Exception ex){
            	logger.info("Exception caught " + ex.getMessage());
            }finally{
            	if(kafkaConsumer != null) kafkaConsumer.close();
                logger.info("After closing KafkaConsumer");
            }
		}
	}
}
