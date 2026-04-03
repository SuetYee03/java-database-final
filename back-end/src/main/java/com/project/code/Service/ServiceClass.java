package com.project.code.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.code.Model.Product;
import com.project.code.Repo.ProductRepository;

@Service
public class ServiceClass {

    @Autowired
    private ProductRepository productRepository;

    public boolean validateProduct(Product product) {
        if (product == null) {
            return false;
        }

        if (product.getSku() != null && !product.getSku().isBlank()) {
            Product existing = productRepository.findBySku(product.getSku());
            return existing != null;
        }

        Product existing = productRepository.findByName(product.getName());
        return existing != null;
    }

    public boolean validateProductId(Long id) {
        return productRepository.existsById(id);
    }
}