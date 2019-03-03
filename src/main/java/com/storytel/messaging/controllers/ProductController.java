package com.storytel.messaging.controllers;

import java.net.URI;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.storytel.messaging.domain.Product;
import com.storytel.messaging.exception.ResourceNotFoundException;
import com.storytel.messaging.services.ProductService;

/**
 * Created by Riju Francis.
 */
@RestController
public class ProductController {

	private static final Logger log = LogManager.getLogger();

	private ProductService productService;

	@Autowired
	public void setProductService(ProductService productService) {
		this.productService = productService;
	}

	/**
	 * create unique session key as a string to identify client.
	 * 
	 */
	@GetMapping("/customer/session")

	public String createSession(HttpServletRequest httpServletRequest, HttpSession session) {

		log.debug("sessionkey from session ::" + session.getId());
		return session.getId();

	}

	/**
	 * send a message to inmemory queue for create operation,if the session is
	 * valid, persist in inmemory message store
	 * 
	 * @throws ResourceNotFoundException
	 * 
	 */

	@PostMapping("/product/sendMessage/{operation}")
	public ResponseEntity<Object> indexProductCreate(@RequestBody Product product, @PathVariable String operation,
			@RequestParam("sessionkey") String sessionkey, HttpServletRequest httpServletRequest)
			throws ResourceNotFoundException {
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("product/sendMessage/")
				.build(product.getId());
		if (productService.validSession(sessionkey, httpServletRequest)) {
			productService.sendMessage(product, null, operation);
			log.info("sendMessage completed " + operation + " " + product.toString());
		} else {
			throw new ResourceNotFoundException("Invalid Session");
		}

		return ResponseEntity.created(location).build();
	}

	/**
	 * list all messages if the session is valid from inmemory message store
	 * 
	 * @throws ResourceNotFoundException
	 * 
	 */
	@GetMapping("/product/list")
	public List<Product> listAllProducts(@RequestParam("sessionkey") String sessionkey,
			HttpServletRequest httpServletRequest) throws ResourceNotFoundException {
		if (productService.validSession(sessionkey, httpServletRequest)) {
			List<Product> allProducts = productService.listAll();
			if (allProducts.isEmpty())
				throw new ResourceNotFoundException(
						"Products Not Found ,Please initiate a send message to Create a Product");
			return allProducts;
		} else {
			throw new ResourceNotFoundException("Invalid Session");
		}

	}

	/**
	 * send a message to inmemory queue for update operation, if the session is
	 * valid ,persist in inmemory message store
	 * 
	 * @throws ResourceNotFoundException
	 * 
	 */
	@PutMapping("/product/sendMessage/{operation}/{id}")
	public ResponseEntity<Object> indexProductUpdate(@RequestBody Product product, @PathVariable String id,
			@PathVariable String operation, @RequestParam("sessionkey") String sessionkey,
			HttpServletRequest httpServletRequest) throws ResourceNotFoundException {
		if (productService.validSession(sessionkey, httpServletRequest)) {
			product.setId(Long.valueOf(id));
			productService.sendMessage(product, id, operation);
			log.info("sendMessage completed " + operation);
		} else
			throw new ResourceNotFoundException("Invalid Session");

		return ResponseEntity.noContent().build();
	}

	/**
	 * send a message to inmemory queue for delete operation, if the session is
	 * valid ,delete from inmemory message store
	 * 
	 * @throws ResourceNotFoundException
	 * 
	 */
	@DeleteMapping("/product/sendMessage/{operation}/{id}")
	public ResponseEntity<Object> indexProductDelete(@PathVariable String id,
			@RequestParam("sessionkey") String sessionkey, HttpServletRequest httpServletRequest,
			@PathVariable String operation) throws ResourceNotFoundException {
		if (productService.validSession(sessionkey, httpServletRequest)) {
			productService.sendMessage(null, id, operation);
			log.info("sendMessage completed " + operation);
		} else
			throw new ResourceNotFoundException("Invalid Session");

		return ResponseEntity.noContent().build();
	}

	/**
	 * Invalidate session manually and clear out inmemory store.
	 * 
	 */
	@GetMapping("/customer/logout")
	public void logoutSession(HttpSession session, HttpServletRequest httpServletRequest) {
		session.invalidate();
		httpServletRequest.getSession().invalidate();
		productService.deleteAll();
	}

}
