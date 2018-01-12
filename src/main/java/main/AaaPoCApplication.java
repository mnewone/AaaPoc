package main;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AaaPoCApplication {
	private static final Logger log = LoggerFactory.getLogger(AaaPoCApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(AaaPoCApplication.class, args);
	}
}
