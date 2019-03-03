package com.storytel.messaging.controllers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;

import com.storytel.messaging.domain.Product;
import com.storytel.messaging.exception.ResourceNotFoundException;
import com.storytel.messaging.repositories.ProductRepository;
import com.storytel.messaging.services.ProductService;
import com.storytel.messaging.services.ProductServiceImpl;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ProductControllerTests {

	@InjectMocks
	ProductController productController = new ProductController();
	ProductService productService = new ProductServiceImpl();
	MockHttpServletRequest mockHttpServletRequest = new MockHttpServletRequest();
	MockHttpSession mockHttpSession = new MockHttpSession();
	JmsTemplate jmsTemplate = mock(JmsTemplate.class); 
	ProductRepository productRepository = mock(ProductRepository.class);

	@Before
    public void init(){
		ReflectionTestUtils.setField(productController, "productService", productService);
		ReflectionTestUtils.setField(productService, "jmsTemplate", jmsTemplate);
		ReflectionTestUtils.setField(productService, "productRepository", productRepository);
	}
	@Test
	public void createSessionTest() {
		assertThat(productController).isNotNull();
		String sessionid = productController.createSession(mockHttpServletRequest, new MockHttpSession());
		assertTrue(!sessionid.isEmpty());
	}

	@Test
	public void indexProductCreateTest() throws ResourceNotFoundException {
		mockHttpServletRequest.setRequestedSessionId("1");
		when(productController.indexProductCreate(new Product(), "create", "1", mockHttpServletRequest))
						.thenCallRealMethod();
	}
	@Test
	public void indexProductUpdateTest() throws ResourceNotFoundException {
		mockHttpServletRequest.setRequestedSessionId("1");
		when(productController.indexProductUpdate(new Product(),"1", "update", "1", mockHttpServletRequest))
						.thenCallRealMethod();
	}
	@Test(expected = ResourceNotFoundException.class)
	public void listAllProducts() throws ResourceNotFoundException{
		mockHttpServletRequest.setRequestedSessionId("1");
		
		List<Product> allProducts= productController.listAllProducts("1",mockHttpServletRequest);
		assertThat(allProducts.isEmpty());
	}
	@Test
	public void indexProductDeletetest() throws ResourceNotFoundException {
		mockHttpServletRequest.setRequestedSessionId("1");
		when(productController.indexProductDelete("1", "1", mockHttpServletRequest,"update"))
						.thenCallRealMethod();
	}
	@Test
	public void logoutSessionTest() {
		assertThat(productController).isNotNull();
		productController.logoutSession(mockHttpSession,mockHttpServletRequest);
		assertTrue(mockHttpSession.isInvalid());
	}
}
