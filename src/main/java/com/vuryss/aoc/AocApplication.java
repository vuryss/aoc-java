package com.vuryss.aoc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Configuration;

@SpringBootApplication
@Configuration
@EnableCaching
public class AocApplication {
	public static void main(String[] args) {
		SpringApplication.run(AocApplication.class, args);
	}

}
