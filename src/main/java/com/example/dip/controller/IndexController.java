package com.example.dip.controller;

import com.example.dip.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;


@Controller
@RequiredArgsConstructor
@Slf4j
public class IndexController {

    private final CategoryService categoryService;
    private final ItemService itemService;
    private final UserServiceImpl userService;



    @GetMapping("/home")
    public String index(Model model, Principal principal){
        ShopController.cardCount(model, principal, userService);
        model.addAttribute("items",itemService.findAll());
        model.addAttribute("categories",categoryService.findAll());
        return "index";
    }


}
