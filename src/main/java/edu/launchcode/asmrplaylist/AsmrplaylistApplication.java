package edu.launchcode.asmrplaylist;

import org.apache.catalina.authenticator.jaspic.AuthConfigFactoryImpl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.security.auth.message.config.AuthConfigFactory;

@SpringBootApplication
public class AsmrplaylistApplication {

	public static void main(String[] args) {

		SpringApplication.run(AsmrplaylistApplication.class, args);
	}
}
