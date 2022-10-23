package com.omarmohseni.microservices.productservice.repository;

import com.omarmohseni.microservices.productservice.model.Product;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ProductRepository extends MongoRepository<Product, String> {
}
