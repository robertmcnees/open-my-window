package com.openmywindow.arbiter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class ArbiterApplication {

	public static void main(String[] args) {
		SpringApplication.run(ArbiterApplication.class, args);
	}

}
