package com.book;

import java.util.TimeZone;

import javax.annotation.PostConstruct;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.book.security.AppProperties;

import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EntityScan(basePackageClasses = { 
		BookManagementApplication.class,
		Jsr310JpaConverters.class 
})
@EnableConfigurationProperties(AppProperties.class)
@EnableSwagger2
@EnableWebMvc
public class BookManagementApplication {

	@PostConstruct
	void init() {
		TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
	}

	public static void main(String[] args) {
		SpringApplication.run(BookManagementApplication.class, args);
	}
}