package com.contenttree;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
//@EntityScan("com.contenttree.userdatastorage")
//@EnableJpaRepositories("com.contenttree.userdatastorage")
@EntityScan(basePackages = "com.contenttree")
public class ContenttreeApplication {

	public static void main(String[] args) {
		SpringApplication.run(ContenttreeApplication.class, args);
	}

}
