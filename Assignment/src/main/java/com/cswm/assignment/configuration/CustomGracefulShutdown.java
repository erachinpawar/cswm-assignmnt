package com.cswm.assignment.configuration;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.catalina.connector.Connector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.embedded.tomcat.TomcatConnectorCustomizer;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;

import com.cswm.assignment.ApplicationConstants;

public class CustomGracefulShutdown implements TomcatConnectorCustomizer, ApplicationListener<ContextClosedEvent> {

	private static final Logger logger = LoggerFactory.getLogger(CustomGracefulShutdown.class);

	private volatile Connector connector;

	@Override
	public void customize(Connector connector) {
		this.connector = connector;
	}

	@Override
	public void onApplicationEvent(ContextClosedEvent event) {
		logger.warn("Application is shutting down gracefully..");
		Executor executor = this.connector.getProtocolHandler().getExecutor();
		if (executor instanceof ThreadPoolExecutor) {
			try {
				ThreadPoolExecutor threadPoolExecutor = (ThreadPoolExecutor) executor;
				threadPoolExecutor.shutdown();
				if (threadPoolExecutor.getActiveCount() > 0 && !threadPoolExecutor.awaitTermination(ApplicationConstants.GRACEFUL_SHUTDOWN_WINDOW, TimeUnit.SECONDS)) {
					logger.warn("Tomcat executor threadpool unable to complete the task within 30 seconds. Proceeding with forceful shutdown");
				}
			} catch (InterruptedException ex) {
				Thread.currentThread().interrupt();
			}
		}
	}

}
