package com.MoleLaw_backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EntityScan(basePackages = "com.MoleLaw_backend.domain.entity")
public class MoleLawBackendApplication {
	public static void main(String[] args) {
		SpringApplication.run(MoleLawBackendApplication.class, args);
	}
}
