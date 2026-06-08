package br.com.fiap.starage;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class StarageApplication {

	public static void main(String[] args) {
		SpringApplication.run(StarageApplication.class, args);
	}

}
