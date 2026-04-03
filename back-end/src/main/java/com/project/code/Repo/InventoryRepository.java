package com.project.code.Repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.code.Model.Inventory;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Long> {

    Inventory findByProduct_IdAndStore_Id(Long productId, Long storeId);

    List<Inventory> findByStore_Id(Long storeId);

    void deleteByProduct_Id(Long productId);
}