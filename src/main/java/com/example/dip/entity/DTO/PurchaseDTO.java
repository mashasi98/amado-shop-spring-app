package com.example.dip.entity.DTO;

import com.example.dip.entity.Card;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PurchaseDTO {

    private String fio;
    private String country;
    private String address;
    private String comment;
    private Card card;
    private Long sum;

}
