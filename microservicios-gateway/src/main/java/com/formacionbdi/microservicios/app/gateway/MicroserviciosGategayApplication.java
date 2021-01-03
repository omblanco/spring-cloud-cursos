package com.formacionbdi.microservicios.app.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@EnableEurekaClient
@SpringBootApplication
public class MicroserviciosGategayApplication {

	public static void main(String[] args) {
		SpringApplication.run(MicroserviciosGategayApplication.class, args);
	}

}
