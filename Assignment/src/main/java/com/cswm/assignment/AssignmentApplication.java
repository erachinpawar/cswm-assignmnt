package com.cswm.assignment;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;

import com.cswm.assignment.configuration.CustomGracefulShutdown;

import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EntityScan(basePackageClasses = { AssignmentApplication.class, Jsr310JpaConverters.class })

@SpringBootApplication
@EnableSwagger2
public class AssignmentApplication {

	public static void main(String[] args) {
		SpringApplication.run(AssignmentApplication.class, args);
	}

	@Bean
	public WebServerFactoryCustomizer<?> tomcatCustomizer() {
		return factory -> {
			if (factory instanceof TomcatServletWebServerFactory) {
				((TomcatServletWebServerFactory) factory).addConnectorCustomizers(gracefulShutdown());
			}
		};
	}

	@Bean
	public CustomGracefulShutdown gracefulShutdown() {
		return new CustomGracefulShutdown();
	}

}
