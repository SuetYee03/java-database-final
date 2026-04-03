package com.project.code.Controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.code.Model.Product;
import com.project.code.Repo.InventoryRepository;
import com.project.code.Repo.ProductRepository;
import com.project.code.Service.ServiceClass;

@RestController
@RequestMapping("/product")
public class ProductController {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ServiceClass serviceClass;

    @Autowired
    private InventoryRepository inventoryRepository;

    @PostMapping
    public Map<String, Object> addProduct(@RequestBody Product product) {
        Map<String, Object> response = new HashMap<>();

        try {
            boolean exists = serviceClass.validateProduct(product);

            if (exists) {
                response.put("message", "Product already exists");
                return response;
            }

            Product savedProduct = productRepository.save(product);
            response.put("message", "Product added successfully");
            response.put("product", savedProduct);

        } catch (DataIntegrityViolationException e) {
            response.put("message", "Failed to add product due to database constraint violation");
        } catch (Exception e) {
            response.put("message", "Failed to add product");
        }

        return response;
    }

    @GetMapping("/{id}")
    public Map<String, Object> getProductbyId(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();

        Optional<Product> product = productRepository.findById(id);

        if (product.isPresent()) {
            response.put("products", product.get());
        } else {
            response.put("message", "Product not found");
        }

        return response;
    }

    @PutMapping
    public Map<String, Object> updateProduct(@RequestBody Product product) {
        Map<String, Object> response = new HashMap<>();

        productRepository.save(product);
        response.put("message", "Product updated successfully");

        return response;
    }

    @GetMapping("/category/{name}/{category}")
    public Map<String, Object> filterbyCategoryProduct(@PathVariable String name,
                                                       @PathVariable String category) {
        Map<String, Object> response = new HashMap<>();
        List<Product> products;

        boolean nameNull = "null".equalsIgnoreCase(name);
        boolean categoryNull = "null".equalsIgnoreCase(category);

        if (nameNull && categoryNull) {
            products = productRepository.findAll();
        } else if (nameNull) {
            products = productRepository.findByCategory(category);
        } else if (categoryNull) {
            products = productRepository.findProductBySubName(name);
        } else {
            products = productRepository.findProductBySubNameAndCategory(name, category);
        }

        response.put("products", products);
        return response;
    }

    @GetMapping
    public Map<String, Object> listProduct() {
        Map<String, Object> response = new HashMap<>();
        List<Product> products = productRepository.findAll();
        response.put("products", products);
        return response;
    }

    @GetMapping("/filter/{category}/{storeid}")
    public Map<String, Object> getProductbyCategoryAndStoreId(@PathVariable String category,
                                                              @PathVariable Long storeid) {
        Map<String, Object> response = new HashMap<>();
        List<Product> products = productRepository.findProductByCategory(category, storeid);
        response.put("product", products);
        return response;
    }

    @DeleteMapping("/{id}")
    public Map<String, Object> deleteProduct(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();

        boolean valid = serviceClass.validateProductId(id);

        if (!valid) {
            response.put("message", "Product not found");
            return response;
        }

        inventoryRepository.deleteByProduct_Id(id);
        productRepository.deleteById(id);

        response.put("message", "Product deleted successfully");
        return response;
    }

    @GetMapping("/searchProduct/{name}")
    public Map<String, Object> searchProduct(@PathVariable String name) {
        Map<String, Object> response = new HashMap<>();
        List<Product> products = productRepository.findProductBySubName(name);
        response.put("products", products);
        return response;
    }
}