package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.example.demo.service.AccountService;
import org.slf4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration

@EnableTransactionManagement

@EnableJpaRepositories

//@SpringBootApplication(exclude = { org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class })

@SpringBootApplication
public class DemoApplication {

	private static final Logger logger = org.slf4j.LoggerFactory.getLogger("accounts-service");


	public static void main(String[] args) {

		//SpringApplication.run(DemoApplication.class, args);

		//init / boot phase
		logger.info("-".repeat(50));
		ConfigurableApplicationContext applicationContext = null;
		applicationContext = SpringApplication.run(DemoApplication.class, args);
		logger.info("-".repeat(50));

		//AccountService accountService = applicationContext.getBean("accountService", AccountService.class);
		AccountService accountService = applicationContext.getBean(AccountService.class);

		//accountService.createAccount("Bob");

		logger.info("-".repeat(50));
		//shutdown
		logger.info("-".repeat(50));

	}

}
