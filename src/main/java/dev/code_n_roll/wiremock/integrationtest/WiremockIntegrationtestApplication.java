package dev.code_n_roll.wiremock.integrationtest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

@SpringBootApplication
public class WiremockIntegrationtestApplication {

	public static void main(String[] args) {
		SpringApplication.run(WiremockIntegrationtestApplication.class, args);
	}

	@Bean
	public RestTemplate restTemplate(){
		return new RestTemplateBuilder()
				.setConnectTimeout(Duration.ofSeconds(5L))
				.setReadTimeout(Duration.ofSeconds(5L))
				.build();
	}
}
