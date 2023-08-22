package com.internship.deltasmartsoftware;

import com.internship.deltasmartsoftware.config.CustomJpaRepositoryFactoryBean;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@OpenAPIDefinition(
		servers = {
				@Server(url = "/", description = "Default Server URL")
		}
)

@SpringBootApplication
@EnableJpaRepositories(repositoryFactoryBeanClass = CustomJpaRepositoryFactoryBean.class)
public class DeltasmartsoftwareApplication {

	public static void main(String[] args) {
		SpringApplication.run(DeltasmartsoftwareApplication.class, args);
	}

}