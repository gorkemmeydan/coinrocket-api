package com.gorkemmeydan.coinrocketapi;

import com.gorkemmeydan.coinrocketapi.repository.impl.CustomRepositoryImpl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(repositoryBaseClass = CustomRepositoryImpl.class)
public class CoinrocketApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(CoinrocketApiApplication.class, args);
	}

}
