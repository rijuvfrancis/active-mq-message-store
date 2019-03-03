package com.storytel.messaging.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.storytel.messaging.domain.Product;


/**
 * @author Riju
 *
 */
public interface ProductRepository extends JpaRepository<Product, Long> {
}
