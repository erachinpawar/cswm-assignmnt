package com.cswm.assignment.configuration;

import javax.annotation.PreDestroy;

import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfig {

	@PreDestroy
	public void onShutDown() {
		System.out.println("Applicatio Shutdown initiated it will be closed in 30 secs ..");

//		try {
//			//Thread.sleep(30000);
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}

		System.out.println("Application closed now ....");
	}

}