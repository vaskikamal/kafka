package com.example.kafkademo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages={"com.example.kafkademo", "com.example.executor"})

public class KafkademoApplication {
	
	public static void main(String[] args) {
		ConfigurableApplicationContext ctx = 
				SpringApplication.run(KafkademoApplication.class, args);
		DemoExample example = ctx.getBean(DemoExample.class);
		example.performDemo();
		
	}
}
