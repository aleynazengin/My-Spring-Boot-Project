package com.example.CrudApplicationUsingJpaMySql;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.reactive.error.DefaultErrorAttributes;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.web.context.request.WebRequest;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Map;
@EnableSwagger2
@SpringBootApplication
public class CrudApplicationUsingJpaMySqlApplication {

	public static void main(String[] args) {
		SpringApplication.run(CrudApplicationUsingJpaMySqlApplication.class, args);
	}

}
