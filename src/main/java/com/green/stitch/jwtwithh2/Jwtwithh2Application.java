package com.green.stitch.jwtwithh2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@EnableMongoRepositories({"com.green.stitch.*"})
@SpringBootApplication
@ComponentScan({"com.green.stitch.*"})
public class Jwtwithh2Application {

	public static void main(String[] args) {
		SpringApplication.run(Jwtwithh2Application.class, args);
	}

}
