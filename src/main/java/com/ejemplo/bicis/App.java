package com.ejemplo.bicis;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@ComponentScan(basePackages = { "com.ejemplo.bicis.controllers", "com.ejemplo.bicis.model",
		"com.ejemplo.bicis.repository", "com.ejemplo.bicis.services" })
@EnableJpaRepositories("com.ejemplo.bicis.repository")
@EnableTransactionManagement
public class App {

	public static void main(String[] args) {
		SpringApplication.run(App.class, args);
	}
}
