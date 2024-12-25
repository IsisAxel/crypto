package com.crypto.crypt;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class CryptApplication {
	public static void main(String[] args) {
		SpringApplication.run(CryptApplication.class, args);
	}
}
