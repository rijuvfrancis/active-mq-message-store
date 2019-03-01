package com.storytel.messaging.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import com.storytel.messaging.ActiveMqMessageStoreRestApiApplication;
import com.storytel.messaging.domain.Product;
import com.storytel.messaging.repositories.ProductRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;


/**
 * @author Riju
 *
 */
@Service
public class ProductServiceImpl implements ProductService {

	private static final Logger log = LoggerFactory.getLogger(ProductServiceImpl.class);

	private ProductRepository productRepository;
	private JmsTemplate jmsTemplate;

	@Autowired
	public ProductServiceImpl(ProductRepository productRepository,
			JmsTemplate jmsTemplate) {
		this.productRepository = productRepository;
		this.jmsTemplate = jmsTemplate;
	}

	@Override
	public List<Product> listAll() {
		List<Product> products = new ArrayList<>();
		productRepository.findAll().forEach(products::add); // fun with Java 8
		return products;
	}

	@Override
	public Product getById(Long id) {
		return productRepository.findById(id).orElse(null);
	}

	@Override
	public Product saveOrUpdate(Product product) {
		productRepository.save(product);
		return product;
	}

	@Override
	public void delete(Long id) {
		productRepository.deleteById(id);

	}

	@Override
	public void deleteAll() {
		productRepository.deleteAll();

	}

	
	@Override
	public void sendMessage(Product product, String id, String operation) {
		Map<String, String> actionmap = new HashMap<>();
		actionmap.put("id", id);
		actionmap.put("operation", operation);
		actionmap.put("product", product != null ? product.toString() : null);
		log.info("Sending the index request through queue message");
		jmsTemplate.convertAndSend(ActiveMqMessageStoreRestApiApplication.PRODUCT_MESSAGE_QUEUE, actionmap);
	}
	/**
	 * Identify the request from client is valid.
	 * 
	 */
	public boolean validSession(String sessionkey, HttpServletRequest httpServletRequest) {
		log.debug("sessionkey ::" + httpServletRequest.getRequestedSessionId());

		if (httpServletRequest.getRequestedSessionId()!= null && httpServletRequest.getRequestedSessionId().equals(sessionkey)) {

			return true;
		}
		log.debug("Invalid Session");
		return false;
	}
}
