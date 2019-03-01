package com.storytel.messaging.listener;

import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.storytel.messaging.ActiveMqMessageStoreRestApiApplication;
import com.storytel.messaging.domain.Product;
import com.storytel.messaging.repositories.ProductRepository;
import com.storytel.messaging.services.ProductService;


/**
 * @author Riju
 *
 */

/**
 * This is the queue listener class, its receiveMessage() method is invoked
 * with the message as the parameter.
 */

@Component
public class MessageListener {

	private ProductRepository productRepository;

	private ProductService productService;

	private static final Logger log = LogManager.getLogger(MessageListener.class);

	public MessageListener(ProductRepository productRepository, ProductService productService) {
		this.productRepository = productRepository;
		this.productService = productService;
	}

	/**
	 * This method is invoked whenever any new message is put in the queue.
	 * 
	 * @param message
	 */
	@JmsListener(destination = ActiveMqMessageStoreRestApiApplication.PRODUCT_MESSAGE_QUEUE, containerFactory = "jmsFactory")
	public void receiveMessage(Map<String, String> message) {
		log.info("Received <" + message + ">");
		String operation = String.valueOf(message.get("operation"));

		if (operation.equals("delete")) {
			Long id = Long.valueOf(message.get("id"));
			productService.delete(id);
			return;
		}
		
		createOrUpdate(message, operation);

		log.info("Message processed...");
	}

	/**
	 * @param message
	 * @param operation
	 */
	private void createOrUpdate(Map<String, String> message, String operation) {
		Product productinmemorydb = null;
		Product productpayload = null;
		if (operation.equals("create") || operation.equals("update")) {

			String productdata = String.valueOf(message.get("product"));
			productpayload = new Gson().fromJson(productdata, Product.class);

			if (operation.equals("update")) {
				Long id = Long.valueOf(message.get("id"));
				productinmemorydb = productRepository.findById(id).orElse(null);
				productpayload.setMessageCount(productinmemorydb.getMessageCount());
			}

			//productService.saveOrUpdate(productpayload);

			productpayload.setMessageReceived(true);
			productpayload.setMessageCount(productpayload.getMessageCount() + 1);
			productRepository.save(productpayload);
		}
	}
}
