package com.gorkemmeydan.coinrocketapi;

import com.gorkemmeydan.coinrocketapi.repository.impl.CustomRepositoryImpl;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EnableJpaRepositories(repositoryBaseClass = CustomRepositoryImpl.class)
@EnableHystrixDashboard
@EnableHystrix
public class CoinrocketApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(CoinrocketApiApplication.class, args);
	}

	@Bean
	public RestTemplate getRestTemplate() {
		return new RestTemplate();
	}

	@Bean
	public OpenAPI customOpenAPI(@Value("${application-title}") String title,
								 @Value("${application-description}") String description,
								 @Value("${application-version}") String version,
								 @Value("${application-licence}") String licence){
		return new OpenAPI()
				.info(new Info()
						.title(title)
						.version(version)
						.description(description)
						.license(new License().name(licence)));
	}
}
