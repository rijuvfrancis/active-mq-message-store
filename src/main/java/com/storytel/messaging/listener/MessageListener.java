package com.storytel.messaging.listener;

import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.storytel.messaging.ActiveMqMessageStoreRestApiApplication;
import com.storytel.messaging.domain.Product;
import com.storytel.messaging.exception.ResourceNotFoundException;
import com.storytel.messaging.repositories.ProductRepository;
import com.storytel.messaging.services.ProductService;

/**
 * @author Riju
 *
 */

/**
 * This is the queue listener class, its receiveMessage() method is invoked with
 * the message as the parameter.
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
	 * @throws ResourceNotFoundException 
	 */
	@JmsListener(destination = ActiveMqMessageStoreRestApiApplication.PRODUCT_MESSAGE_QUEUE, containerFactory = "jmsFactory")
	public void receiveMessage(Map<String, String> message) throws ResourceNotFoundException {
		
		try {
		log.info("Received <" + message + ">");
		String operation = String.valueOf(message.get("operation"));

		if (operation.equals("delete")) {
			Long id = Long.valueOf(message.get("id"));
			productService.delete(id);
			log.info("Message processed...");
			return;
		}

		createOrUpdate(message, operation);

		log.info("Message processed...");
		}
		catch(DataAccessException e){
			log.info("!!!! No Product found for  Delete with given id !!!!  ??? can you pass the correct id ??");
		}
	}

	/**
	 * @param message
	 * @param operation
	 */
	private void createOrUpdate(Map<String, String> message, String operation) {
		Product productfrominmemorydb = null;
		Product productfrompayload = null;
		if (operation.equals("create") || operation.equals("update")) {

			String productdata = String.valueOf(message.get("product"));
			productfrompayload = new Gson().fromJson(productdata, Product.class);

			if (operation.equals("update")) {
				Long id = Long.valueOf(message.get("id"));
				productfrominmemorydb = productRepository.findById(id).orElse(null);

				if (productfrominmemorydb == null) {
					log.info("!!!! No Product found for  Update with id !!!! " + id
							+ " ??? can you pass the correct id ??");
					return;
				}
				productfrompayload.setMessageCount(productfrominmemorydb.getMessageCount());
			}

			productfrompayload.setMessageReceived(true);
			productfrompayload.setMessageCount(productfrompayload.getMessageCount() + 1);
			productRepository.save(productfrompayload);
		}
	}
}
