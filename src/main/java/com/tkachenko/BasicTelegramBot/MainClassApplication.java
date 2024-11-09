package com.tkachenko.BasicTelegramBot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.tkachenko.BasicTelegramBot")
public class MainClassApplication {
	public static void main(String[] args) {
		SpringApplication.run(MainClassApplication.class, args);
	}
}
