package com.example.dip.controller;

import com.example.dip.entity.*;
import com.example.dip.entity.DTO.CartItemDTO;
import com.example.dip.entity.DTO.UserDTO;
import com.example.dip.service.Interface.CardService;
import com.example.dip.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;


@Controller
@RequestMapping("/user")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserServiceImpl userService;
    private final CardService cardService;
    private final ItemService itemService;
    private final ItemCartService itemCartService;
    private final PurchaseService purchaseService;
    private final PurchaseItemService purchaseItemService;


    @PreAuthorize("isAuthenticated()")
    @GetMapping("/profile")
    public String profileUser(Model model, Principal principal) {
        log.error(principal.getName());
        if (principal == null) {
            throw new RuntimeException("You are not authorize");
        }
        ShopController.cardCount(model, principal, userService);

        return "profile";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/profile/change")
    public String changeUser(Model model, Principal principal) {
        log.error(principal.getName());
        if (principal == null) {
            throw new RuntimeException("You are not authorize");
        }
        ShopController.cardCount(model, principal, userService);
        UserDTO user = new UserDTO();
        model.addAttribute("user", user);
        return "password";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/profile/update")
    public String updateProfileUser(@ModelAttribute UserDTO user, Model model, Principal principal) {
        log.error(user.toString());
        if (principal == null) {
            throw new RuntimeException("You are not authorize");
        }
        if (user.getPassword() == null || user.getPassword().length() < 7) {
            model.addAttribute("chngPassError", true);
            return "password";
        }
        User userUpdate = userService.findByEmail(principal.getName());
        userUpdate.setPassword(user.getPassword());
        userService.saveUser(userUpdate);
        model.addAttribute("okChange", true);
        return "password";

    }


    @PreAuthorize("isAuthenticated()")
    @GetMapping("/profile/purchases")
    public String purchaseUser(Model model, Principal principal) {
        log.error(principal.getName());
        if (principal == null) {
            throw new RuntimeException("You are not authorize");
        }
        User user = userService.findByEmail(principal.getName());
        List<Purchase> purchases = new ArrayList<>(user.getPurchases());
        ShopController.cardCount(model, principal, userService);
        model.addAttribute("purchases", purchases);
        return "purchases";
    }


    @PreAuthorize("isAuthenticated()")
    @GetMapping("profile/purchases/{id}")
    public String product(Model model, @PathVariable Integer id, Principal principal) {

        Purchase purchase = purchaseService.findById(Long.valueOf(id));
        List<CartItemDTO> items = new ArrayList<>();
        if (purchase != null) {
            List<PurchaseItem> card_items = purchase.getPurchaseItems();

            for (PurchaseItem item : card_items) {

                items.add(
                        CartItemDTO.builder()
                                .itemCardId(item.getId())
                                .title(item.getItem().getTitle())
                                .image(item.getItem().getImage())
                                .price(Long.valueOf(item.getItem().getPrice()))
                                .count(Long.valueOf(item.getCount()))
                                .build()
                );
            }
            log.error(items.toString());
            model.addAttribute("purchase", purchase);



        } else {
            log.error("purchase is empty");
        }
        ShopController.cardCount(model, principal, userService);
        model.addAttribute("items", items);
        return "purchaseDetails";
    }


    @PreAuthorize("isAuthenticated()")
    @GetMapping("/profile/favorite")
    public String favoriteUser(Model model, Principal principal) {
        log.error(principal.getName());
        if (principal == null) {
            throw new RuntimeException("You are not authorize");
        }
        User user = userService.findByEmail(principal.getName());
        List<Item> items = new ArrayList<Item>(user.getItems());
        ShopController.cardCount(model, principal, userService);
        model.addAttribute("purchases", items);
        return "favorite";
    }


}
