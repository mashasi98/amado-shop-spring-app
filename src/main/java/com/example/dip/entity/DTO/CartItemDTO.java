package com.example.dip.entity.DTO;


import com.example.dip.entity.Category;
import jdk.jfr.DataAmount;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@AllArgsConstructor
public class CartItemDTO {
    private Long itemCardId;
    private String title;
    private String image;
    private Long price;
    private Long count;

}
