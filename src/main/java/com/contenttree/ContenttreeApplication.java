package com.contenttree;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan(basePackages = "com.contenttree")
public class ContenttreeApplication {

	public static void main(String[] args) {
		SpringApplication.run(ContenttreeApplication.class, args);
	}

}
