package com.project.code.Controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.code.Model.CombinedRequest;
import com.project.code.Model.Inventory;
import com.project.code.Model.Product;
import com.project.code.Repo.InventoryRepository;
import com.project.code.Repo.ProductRepository;
import com.project.code.Service.ServiceClass;

@RestController
@RequestMapping("/inventory")
public class InventoryController {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private InventoryRepository inventoryRepository;

    @Autowired
    private ServiceClass serviceClass;

    @PutMapping("/update")
    public Map<String, String> updateInventory(@RequestBody CombinedRequest request) {
        Map<String, String> response = new HashMap<>();

        Product product = request.getProduct();
        Inventory inventory = request.getInventory();

        if (product == null || product.getId() == null) {
            response.put("message", "Invalid product data");
            return response;
        }

        boolean validProduct = serviceClass.validateProductId(product.getId());
        if (!validProduct) {
            response.put("message", "Product not found");
            return response;
        }

        Inventory existingInventory = inventoryRepository.findByProduct_IdAndStore_Id(
                product.getId(),
                inventory.getStore().getId()
        );

        if (existingInventory != null) {
            existingInventory.setStockLevel(inventory.getStockLevel());
            inventoryRepository.save(existingInventory);
            response.put("message", "Inventory updated successfully");
        } else {
            response.put("message", "No inventory data available");
        }

        return response;
    }

    @PostMapping("/save")
    public Map<String, String> saveInventory(@RequestBody Inventory inventory) {
        Map<String, String> response = new HashMap<>();

        if (inventory == null || inventory.getProduct() == null || inventory.getStore() == null) {
            response.put("message", "Invalid inventory data");
            return response;
        }

        Inventory existingInventory = inventoryRepository.findByProduct_IdAndStore_Id(
                inventory.getProduct().getId(),
                inventory.getStore().getId()
        );

        if (existingInventory != null) {
            response.put("message", "Inventory already exists");
        } else {
            inventoryRepository.save(inventory);
            response.put("message", "Inventory saved successfully");
        }

        return response;
    }

    @GetMapping("/products/{storeId}")
    public Map<String, List<Product>> getAllProducts(@PathVariable Long storeId) {
        Map<String, List<Product>> response = new HashMap<>();
        List<Product> products = productRepository.findProductsByStoreId(storeId);
        response.put("products", products);
        return response;
    }

    @GetMapping("/filter/{storeId}/{category}/{name}")
    public Map<String, List<Product>> getProductName(@PathVariable Long storeId,
                                                     @PathVariable String category,
                                                     @PathVariable String name) {
        Map<String, List<Product>> response = new HashMap<>();
        List<Product> products;

        boolean categoryNull = "null".equalsIgnoreCase(category);
        boolean nameNull = "null".equalsIgnoreCase(name);

        if (categoryNull && nameNull) {
            products = productRepository.findProductsByStoreId(storeId);
        } else if (categoryNull) {
            products = productRepository.findByNameLike(storeId, name);
        } else if (nameNull) {
            products = productRepository.findByCategoryAndStoreId(storeId, category);
        } else {
            products = productRepository.findByNameAndCategory(storeId, name, category);
        }

        response.put("product", products);
        return response;
    }

    @GetMapping("/search/{storeId}/{name}")
    public Map<String, List<Product>> searchProduct(@PathVariable Long storeId,
                                                    @PathVariable String name) {
        Map<String, List<Product>> response = new HashMap<>();
        List<Product> products = productRepository.findByNameLike(storeId, name);
        response.put("product", products);
        return response;
    }

    @DeleteMapping("/remove/{productId}")
    public Map<String, String> removeProduct(@PathVariable Long productId) {
        Map<String, String> response = new HashMap<>();

        Product product = productRepository.findById(productId).orElse(null);

        if (product == null) {
            response.put("message", "Product not found");
            return response;
        }

        inventoryRepository.deleteByProduct_Id(productId);
        productRepository.deleteById(productId);

        response.put("message", "Product deleted successfully");
        return response;
    }

    @GetMapping("/validate/{storeId}/{productId}/{quantity}")
    public boolean validateQuantity(@PathVariable Long storeId,
                                    @PathVariable Long productId,
                                    @PathVariable Integer quantity) {

        Inventory inventory = inventoryRepository.findByProduct_IdAndStore_Id(productId, storeId);

        if (inventory == null || inventory.getStockLevel() == null) {
            return false;
        }

        return inventory.getStockLevel() >= quantity;
    }
}