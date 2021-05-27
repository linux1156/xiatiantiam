package com.example.demo;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication()
@MapperScan("com.example.demo.dao.db")
@ComponentScan({"com.example.demo.controller","com.example.demo.service","com.example.demo.service.impl", "com.example.demo.dao.db", "com.example.demo.util","com.example.demo.config"})
@EnableScheduling
@EnableCaching
public class DemoApplication {
	public static void main(String[] args) {

		SpringApplication.run(DemoApplication.class, args);
	}

}
