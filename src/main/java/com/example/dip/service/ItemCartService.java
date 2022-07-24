package com.example.dip.service;


import com.example.dip.entity.Card;
import com.example.dip.entity.CardItem;
import com.example.dip.repository.ItemCartRepository;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.LifecycleState;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
// откат всех записей к предыдущему значению, если любая из операций в этом методе завершится неудачей,
// а также повторно бросит оригинальное исключение.
// Это значит, что если добавление одного из людей завершится ошибкой,
// то ни один из людей в итоге не добавится в таблицу BOOKINGS.
public class ItemCartService {

    private final ItemCartRepository itemCartRepository;

    public void updateCount(CardItem item){
        itemCartRepository.save(item);
    }

   public CardItem findById(Long id){

         return itemCartRepository.getById(id);
    }

    public List<CardItem> findAllByCartId(Long id){
        return itemCartRepository.findAllByCard_id(id);
    }

    public void deleteByItem(CardItem item){
        itemCartRepository.delete(item);
    }
}
