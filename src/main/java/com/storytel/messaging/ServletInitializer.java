package com.storytel.messaging;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * @author Riju
 *
 */

/**
 * Required for Tomcat to detect as Spring boot application for hosting in AWS Elastic Bean Stalk.
 * 
 */
public class ServletInitializer extends SpringBootServletInitializer {

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(ActiveMqMessageStoreRestApiApplication.class);
	}

}
