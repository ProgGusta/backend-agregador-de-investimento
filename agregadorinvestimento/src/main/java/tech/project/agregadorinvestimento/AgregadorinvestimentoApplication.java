package tech.project.agregadorinvestimento;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class AgregadorinvestimentoApplication {

	public static void main(String[] args) {
		SpringApplication.run(AgregadorinvestimentoApplication.class, args);
	}

}
