package com.cswm.assignment.configuration;

import javax.annotation.PreDestroy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

import com.cswm.assignment.ApplicationConstants;

@Configuration
public class ApplicationConfig {

	private final Logger logger = LoggerFactory.getLogger(ApplicationConfig.class);
	
	@PreDestroy
	public void onShutDown() {
		
		logger.info(ApplicationConstants.APPLICATION_SHUTDOWN_WARN);
		try {
			Thread.sleep(ApplicationConstants.GRACEFUL_SHUTDOWN_WINDOW);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		logger.info(ApplicationConstants.APPLICATION_SHUTDOWN_MSG);
	}

}