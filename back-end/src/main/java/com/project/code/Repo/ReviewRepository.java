package com.project.code.Repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.code.Model.Review;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    List<Review> findByStoreIdAndProductId(Long storeId, Long productId);
}