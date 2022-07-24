package com.example.dip.controller;

import com.example.dip.entity.*;
import com.example.dip.entity.DTO.AddingItemDto;
import com.example.dip.entity.DTO.CartItemDTO;
import com.example.dip.entity.DTO.PurchaseDTO;
import com.example.dip.service.Interface.CardService;
import com.example.dip.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.example.dip.controller.ShopController.cardCount;

@Controller
@RequestMapping("/favorite")
@RequiredArgsConstructor
@Slf4j
public class FavoriteController {

    private final UserServiceImpl userService;
    private final CardService cardService;
    private final ItemService itemService;
    private final ItemCartService itemCartService;
    private final PurchaseService purchaseService;
    private final PurchaseItemService purchaseItemService;

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/")
    public String favoriteList(Model model, Principal principal) {
        List<Item> items = userService.findByEmail(principal.getName()).getItems();
        cardCount(model, principal,  userService);
        model.addAttribute("items",items);
        model.addAttribute("newItem",new Item());
        return "favorite";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/add")
    public String addFavorite(Model model, Principal principal,
                              @ModelAttribute Item item, HttpServletRequest request) {
        User user = userService.findByEmail(principal.getName());
        List<Item> items= user.getItems();
        int index=0;
        boolean inUserList = false;
        for (Item i:items) {
            if (Objects.equals(i.getId(), item.getId())) {
                inUserList = true;
                break;
            }
            index++;
        }
        if(!inUserList){
          items.add(item);
          userService.saveUser(user);
        }else {
            items.remove(index);
            userService.saveUser(user);
        }
        return getPreviousPageByRequest(request).orElse("/");
    }


    @PreAuthorize("isAuthenticated()")
    @PostMapping("/delete")
    public String deleteFavorite(Model model, Principal principal,
                              @ModelAttribute Item item, HttpServletRequest request) {
        User user = userService.findByEmail(principal.getName());
        List<Item> items= user.getItems();
        int index = 0;
        boolean inUserList = false;
        for (Item i:items) {
            if (Objects.equals(i.getId(), item.getId())) {
                inUserList = true;
                break;
            }
            index++;
        }
        if(inUserList){
            items.remove(index);
            userService.saveUser(user);
        }
        return getPreviousPageByRequest(request).orElse("/");
    }





    public Optional<String> getPreviousPageByRequest(HttpServletRequest request) {
        return Optional.ofNullable(request.getHeader("Referer")).map(requestUrl -> "redirect:" + requestUrl);
    }

}
