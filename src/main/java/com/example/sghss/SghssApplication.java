package com.example.sghss;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class SghssApplication {

	public static void main(String[] args) {
		SpringApplication.run(SghssApplication.class, args);
	}

}
