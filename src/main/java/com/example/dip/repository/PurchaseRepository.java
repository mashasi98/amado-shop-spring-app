package com.example.dip.repository;

import com.example.dip.entity.Purchase;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface PurchaseRepository extends JpaRepository<Purchase,Long> {

    Purchase findByUser_id(Long id);

    @Query("select p from Purchase p where p.order_number = ?1")
    Purchase findByOrder_number(Long id);

;
}
