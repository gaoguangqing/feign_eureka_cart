package com.spoon;

import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
@SpringCloudApplication
@EnableFeignClients
public class CartConsumerApp {
	public static void main(String[] args) {
		SpringApplication.run(CartConsumerApp.class, args);
	}
}
