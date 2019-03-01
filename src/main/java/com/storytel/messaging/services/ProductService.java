package com.storytel.messaging.services;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.storytel.messaging.domain.Product;

/**
 * @author Riju
 *
 */
public interface ProductService {

    List<Product> listAll();

    Product getById(Long id);

    Product saveOrUpdate(Product product);

    void delete(Long id);
    
    void deleteAll();

    void sendMessage(Product product ,String id ,String operation);
    
    boolean validSession(String sessionkey, HttpServletRequest httpServletRequest);
    
}
