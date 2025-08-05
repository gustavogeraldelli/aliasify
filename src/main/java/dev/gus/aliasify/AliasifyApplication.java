package dev.gus.aliasify;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class AliasifyApplication {

	public static void main(String[] args) {
		SpringApplication.run(AliasifyApplication.class, args);
	}

}
