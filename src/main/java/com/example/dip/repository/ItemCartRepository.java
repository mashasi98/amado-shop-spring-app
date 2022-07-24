package com.example.dip.repository;

import com.example.dip.entity.CardItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository

public interface ItemCartRepository extends JpaRepository<CardItem, Long> {

    @Query("select c from CardItem c where c.card.id = :cardId and c.item.id = :itemId")
    CardItem findByCard_idAndItem_id(@Param("cardId") Long cardId, @Param("itemId") Long itemId);

    List<CardItem> findAllByCard_id(Long id);


}
