package com.contenttree;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;

@SpringBootApplication
//@EnableAutoConfiguration(exclude = {UserDetailsServiceAutoConfiguration.class})
public class ContenttreeApplication {

	public static void main(String[] args) {
		SpringApplication.run(ContenttreeApplication.class, args);
	}

}
