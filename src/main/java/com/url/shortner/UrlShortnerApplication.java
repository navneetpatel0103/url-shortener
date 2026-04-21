package com.url.shortner;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableJpaAuditing
@EnableScheduling
@EnableAsync
public class UrlShortnerApplication {

	public static void main(String[] args) {
		System.out.println("Url shortener application started.....");
		SpringApplication.run(UrlShortnerApplication.class, args);
	}

}
