package com.project.code.Controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.project.code.Model.Store;
import com.project.code.Model.PlaceOrderRequestDTO;
import com.project.code.Repo.StoreRepository;
import com.project.code.Service.OrderService;

@RestController
@RequestMapping("/store")
public class StoreController {

    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    private OrderService orderService;

    @PostMapping
    public Map<String, String> addStore(@RequestBody Store store) {
        Map<String, String> response = new HashMap<>();
        storeRepository.save(store);
        response.put("message", "Store created successfully");
        return response;
    }

    @GetMapping("/validate/{storeId}")
    public boolean validateStore(@PathVariable Long storeId) {
        return storeRepository.existsById(storeId);
    }

    @PostMapping("/placeOrder")
    public Map<String, String> placeOrder(@RequestBody PlaceOrderRequestDTO request) {
        Map<String, String> response = new HashMap<>();

        try {
            orderService.placeOrder(request);
            response.put("message", "Order placed successfully");
        } catch (Exception e) {
            response.put("Error", "Error processing order");
        }

        return response;
    }
}