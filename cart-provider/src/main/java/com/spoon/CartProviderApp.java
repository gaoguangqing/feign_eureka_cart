package com.spoon;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
@SpringBootApplication
@EnableEurekaClient
@MapperScan("com.spoon.mapper")
public class CartProviderApp {
	public static void main(String[] args) {
		SpringApplication.run(CartProviderApp.class, args);
	}
}
