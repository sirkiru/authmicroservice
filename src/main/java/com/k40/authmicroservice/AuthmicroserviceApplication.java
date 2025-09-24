package com.k40.authmicroservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class AuthmicroserviceApplication {

	public static void main(String[] args) {
		SpringApplication.run(AuthmicroserviceApplication.class, args);
	}

}
