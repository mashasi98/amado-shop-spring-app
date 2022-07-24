package com.example.dip.repository;


import com.example.dip.entity.Card;
import com.example.dip.entity.Category;
import com.example.dip.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CardRepository extends JpaRepository<Card,Long> {

    Card findByUser_id(Long id);

}
