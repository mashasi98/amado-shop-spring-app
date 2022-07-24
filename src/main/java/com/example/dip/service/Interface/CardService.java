package com.example.dip.service.Interface;

import com.example.dip.entity.Card;
import com.example.dip.entity.CardItem;
import com.example.dip.entity.User;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface CardService  {

    Card findByUser_id(Long id);
    CardItem saveItem(CardItem cardItem);
    CardItem findByCardAndItem(Long cardId,Long itemId);
    void deleteCard(Card card);

    void updateCard(Card card);
}
