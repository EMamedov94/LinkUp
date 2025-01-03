package com.example.linkup;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class LinkUpApplication {

	public static void main(String[] args) {
		SpringApplication.run(LinkUpApplication.class, args);
	}

}
