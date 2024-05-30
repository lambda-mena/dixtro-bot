package com.vaatu.bots.dixtro;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class DixtroApplication {
	// TODO: Add Clear, Queue commands.

	public static void main(String[] args) {
		SpringApplication.run(DixtroApplication.class, args);
	}

}
