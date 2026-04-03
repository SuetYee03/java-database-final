package com.project.code.Service;

import org.springframework.stereotype.Service;

import com.project.code.Model.PlaceOrderRequestDTO;

@Service
public class OrderService {

    public void placeOrder(PlaceOrderRequestDTO request) {
        
        if (request == null) {
            throw new RuntimeException("Invalid order request");
        }
    }
}
