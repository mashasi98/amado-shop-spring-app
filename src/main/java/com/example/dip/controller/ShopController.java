package com.example.dip.controller;

import com.example.dip.entity.Brand;
import com.example.dip.entity.Category;
import com.example.dip.entity.DTO.AddingItemDto;
import com.example.dip.entity.DTO.ItemDTO;
import com.example.dip.entity.DTO.PriceRange;
import com.example.dip.entity.Item;
import com.example.dip.entity.User;
import com.example.dip.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Controller

@RequestMapping("/shop")
@RequiredArgsConstructor
@Slf4j
public class ShopController {

    private final CategoryService categoryService;
    private final ItemService itemService;
    private final UserServiceImpl userService;
    private final BrandService brandService;

    @GetMapping
    public String shop(Model model,Principal principal){
        getPriceRange(model);
        cardCount(model, principal,  userService);
        return showItems(model,
                        brandService.findAll(),
                        itemService.findAll(),
                        categoryService.findAll());
    }

    @PostMapping("/priceRange")
    public String rangeByPrice(Model model, @ModelAttribute PriceRange priceRange,Principal principal){
        List<Item>items =  itemService.findByRange(priceRange.getMin(),priceRange.getMax());
        items.sort(Comparator.comparing(Item::getPrice));
        getPriceRange(model);
        cardCount(model, principal,  userService);
        return showItems(model,
                brandService.findAll(),
                items,
                categoryService.findAll());

    }

    @GetMapping("/{id}")
    public String shopByCategory(Model model, @PathVariable Integer id, Principal principal){
        getPriceRange(model);
        cardCount(model, principal,  userService);
        return showItems(model,
                brandService.findAll(),
                categoryService.findById(Long.valueOf(id)).getItems(),
                categoryService.findAll());
    }

    private void getPriceRange(Model model){

        int min =itemService.min();
        int max = itemService.max();
        model.addAttribute("priceRange", new PriceRange(Math.toIntExact(min), Math.toIntExact(max)));

    }

    private String showItems(Model model, List<Brand> brands, List<Item> items, List<Category> categories){
        model.addAttribute("brands",brands);
        model.addAttribute("items",items);
        model.addAttribute("categories",categories);
        return "shop";

    }

    @GetMapping("/category/{id}")
    public String product(Model model, @PathVariable Integer id,Principal principal){
        Item item= itemService.findById(Long.valueOf(id));
        ItemDTO itemDTO = ItemDTO.builder()
                .id(item.getId())
                .title(item.getTitle())
                .image(item.getImage())
                .otherImage(item.getOther_image())
                .price((long) item.getPrice())
                .description(item.getDescription())
//                .itemCount(item.getCount())
//                .stock(item.isStock())
                .count(1L)
                .build();

        ShopController.cardCount(model, principal, userService);


        model.addAttribute("item",itemDTO);
        model.addAttribute("category",item.getCategory());
        model.addAttribute("addingItem", new AddingItemDto());
        return "product-details";
    }

    public static void cardCount(Model model, Principal principal,  UserServiceImpl userService) {
        if (principal != null){
            log.error("principal not null");
            User user = userService.findByEmail(principal.getName());
            int itemCount = user.getCard().getCardItems().size();
            model.addAttribute("itemCount",itemCount);
            model.addAttribute("userLogin", true);
        }
    }

}
