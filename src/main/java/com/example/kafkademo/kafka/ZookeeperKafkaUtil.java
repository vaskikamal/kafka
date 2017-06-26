package com.example.kafkademo.kafka;

import java.util.List;
import java.util.Properties;

import javax.annotation.PostConstruct;

import kafka.admin.AdminUtils;
import kafka.admin.RackAwareMode;
import kafka.utils.ZKStringSerializer$;
import kafka.utils.ZkUtils;

import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.ZkConnection;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import scala.collection.JavaConversions;

@Component
public class ZookeeperKafkaUtil {
	private static final Logger logger = Logger.getLogger(ZookeeperKafkaUtil.class);
	private String zookeeperHostUrl= "localhost:2181";
	int sessionTimeoutMs = 10 * 1000;
    int connectionTimeoutMs = 8 * 1000;
    private boolean isSecureKafkaCluster=false;
        
	@PostConstruct
	public void init() {
		
	}
	
	public boolean isTopicExists(String topicName) {
		ZkClient zkClient = null;
		try {
			zkClient = getZkClient();
			ZkUtils zkUtils = getKafkaZookeeperUtils(zkClient);
			List<String> topics = JavaConversions.asJavaList(zkUtils.getAllTopics()) ;
			return topics.contains(topicName);
		}finally {
			if(zkClient != null) zkClient.close();
		}
	}
	
	public void createKafkaTopic(String topicName, int replicationFactor, int noPartition) {
		ZkClient zkClient = null;
		try {
		Properties topicConfig = new Properties();
		zkClient = getZkClient();
		ZkUtils zkUtils = getKafkaZookeeperUtils(zkClient);
		AdminUtils.createTopic(zkUtils, topicName, noPartition, 
				replicationFactor, topicConfig, RackAwareMode.Disabled$.MODULE$);
		} finally {
			if(zkClient != null) zkClient.close();
		}
	}

	ZkClient getZkClient() {
		return new ZkClient(getZookeeperHostUrl(), sessionTimeoutMs,
				connectionTimeoutMs, ZKStringSerializer$.MODULE$);
	}
	
	ZkUtils  getKafkaZookeeperUtils(ZkClient zkClient) {
		return new ZkUtils(zkClient, new ZkConnection(getZookeeperHostUrl()), isSecureKafkaCluster());	       
	}
	
	public String getZookeeperHostUrl() {
		return zookeeperHostUrl;
	}

	public boolean isSecureKafkaCluster() {
		return isSecureKafkaCluster;
	}
	
}
