package com.storytel.messaging.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.storytel.messaging.domain.Product;

/**
 * Created by jt on 1/10/17.
 */
public interface ProductRepository extends JpaRepository<Product, Long> {
}
