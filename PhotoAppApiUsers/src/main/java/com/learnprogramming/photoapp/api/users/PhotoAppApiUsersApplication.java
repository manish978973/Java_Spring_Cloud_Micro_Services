package com.learnprogramming.photoapp.api.users;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.client.RestTemplate;

import feign.Logger;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
@EnableCircuitBreaker
public class PhotoAppApiUsersApplication {
	
	@Autowired
	Environment environment;

	public static void main(String[] args) {
		SpringApplication.run(PhotoAppApiUsersApplication.class, args);
	}
	
	@Bean
	public BCryptPasswordEncoder bcryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	
	@Bean
	@LoadBalanced
	public RestTemplate getRestTemplate() {
		return new RestTemplate();
	}
	
	
	@Bean
	@Profile("!production")
	Logger.Level feignLoggerLevel(){
		return Logger.Level.FULL;
	}
	
	@Bean
	@Profile("production")
	public String createproduction() {
		System.out.println("Production bean created." + environment.getProperty("myapplication.environment"));
		return "Production Bean";
	}
	
	@Bean
	@Profile("default")
	public String createdefaultproduction() {
		System.out.println("Production bean created." + environment.getProperty("myapplication.environment"));
		return "Default Bean";
	}
	
	@Bean
	@Profile("!production")
	public String createnotproduction() {
		System.out.println("Production bean created." + environment.getProperty("myapplication.environment"));
		return "Not Production Bean";
	}
	
	@Bean
	@Profile("production")
	Logger.Level feignLoggerproductionLevel(){
		return Logger.Level.NONE;
	}
	


	

}
