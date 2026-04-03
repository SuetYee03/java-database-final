package com.project.code.Model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Column;

@Entity
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long storeId;

    @Column(nullable = false)
    private Long productId;

    @Column(nullable = false)
    private Long customerId;

    @Column(nullable = false)
    private Integer rating;

    @Column(length = 1000)
    private String comment;

    public Review() {
    }

    public Review(Long id, Long storeId, Long productId, Long customerId, Integer rating, String comment) {
        this.id = id;
        this.storeId = storeId;
        this.productId = productId;
        this.customerId = customerId;
        this.rating = rating;
        this.comment = comment;
    }

    public Long getId() {
        return id;
    }

    public Long getStoreId() {
        return storeId;
    }

    public Long getProductId() {
        return productId;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public Integer getRating() {
        return rating;
    }

    public String getComment() {
        return comment;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setStoreId(Long storeId) {
        this.storeId = storeId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}