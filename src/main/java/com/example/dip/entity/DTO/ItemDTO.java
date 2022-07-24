package com.example.dip.entity.DTO;


import com.example.dip.entity.Category;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class ItemDTO {

    private Long id;
    private String title;
    private String image;
    private String otherImage;
    private String description;
    private Long price;
    private Long count;
//    private Long itemCount;
//    private boolean stock;

}
