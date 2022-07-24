package com.example.dip.service;

import com.example.dip.entity.Card;
import com.example.dip.entity.CardItem;
import com.example.dip.entity.User;
import com.example.dip.repository.CardRepository;
import com.example.dip.repository.ItemCartRepository;
import com.example.dip.service.Interface.CardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CardServiceImpl implements CardService {

    private final CardRepository cardRepository;
    private final ItemCartRepository itemCartRepository;

    @Override
    public Card findByUser_id(Long id) {
        return cardRepository.findByUser_id(id);
    }

    @Override
    public CardItem saveItem(CardItem cardItem) {
        return itemCartRepository.save(cardItem);
        
    }

    @Override
    public CardItem findByCardAndItem(Long cardId, Long itemId) {
        return itemCartRepository.findByCard_idAndItem_id(cardId,itemId);
    }

    @Override
    public void deleteCard(Card card) {
        card.setPrice(0);
        card.setCount(0);
        itemCartRepository.deleteAll(card.getCardItems());
    }

    @Override
    public void updateCard(Card card) {
        cardRepository.save(card);
    }


}
