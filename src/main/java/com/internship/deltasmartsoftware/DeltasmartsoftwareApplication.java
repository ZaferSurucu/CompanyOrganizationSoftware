package com.internship.deltasmartsoftware;

import com.internship.deltasmartsoftware.config.CustomJpaRepositoryFactoryBean;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(repositoryFactoryBeanClass = CustomJpaRepositoryFactoryBean.class)
public class DeltasmartsoftwareApplication {

	public static void main(String[] args) {
		SpringApplication.run(DeltasmartsoftwareApplication.class, args);
	}

}
