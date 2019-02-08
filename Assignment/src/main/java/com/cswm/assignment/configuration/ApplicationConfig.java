package com.cswm.assignment.configuration;

import javax.annotation.PreDestroy;

import org.springframework.context.annotation.Configuration;

import com.cswm.assignment.ApplicationConstants;

@Configuration
public class ApplicationConfig {

	@PreDestroy
	public void onShutDown() {
		System.out.println(ApplicationConstants.APPLICATION_SHUTDOWN_WARN);
		try {
			Thread.sleep(ApplicationConstants.GRACEFUL_SHUTDOWN_WINDOW);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		System.out.println(ApplicationConstants.APPLICATION_SHUTDOWN_MSG);
	}

}