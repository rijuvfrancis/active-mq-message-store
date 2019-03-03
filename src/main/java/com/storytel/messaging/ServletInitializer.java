package com.storytel.messaging;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * @author Riju
 *
 */

/**
 * Required to Host Spring boot application in AWS EBS as Tomcat application.
 * 
 */
public class ServletInitializer extends SpringBootServletInitializer {

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(ActiveMqMessageStoreRestApiApplication.class);
	}

}
