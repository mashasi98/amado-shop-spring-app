package com.example.dip.service;

import com.example.dip.entity.PurchaseItem;
import com.example.dip.repository.PurchaseItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PurchaseItemService {

    private final PurchaseItemRepository purchaseItemRepository;

    public void savePurchaseItem(PurchaseItem item){
        purchaseItemRepository.save(item);
    }
}
