package com.example.dip.controller;

//import com.example.dip.Config.MailConfig;
import com.example.dip.entity.*;
import com.example.dip.entity.DTO.AddingItemDto;
import com.example.dip.entity.DTO.CartItemDTO;
import com.example.dip.entity.DTO.PurchaseDTO;
import com.example.dip.entity.customEmail.EmailDetails;
import com.example.dip.service.Interface.CardService;
import com.example.dip.service.*;
import com.example.dip.service.Interface.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.security.Principal;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/cart")
@RequiredArgsConstructor
@Slf4j
public class CartController {

    private final UserServiceImpl userService;
    private final CardService cardService;
    private final ItemService itemService;
    private final ItemCartService itemCartService;
    private final PurchaseService purchaseService;
    private final PurchaseItemService purchaseItemService;
    private final EmailService emailService;

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/")
    public String shoppingCard(Model model, Principal principal) {
        log.error(principal.getName());
        if (principal == null) {
            throw new RuntimeException("You are not authorize");
        }

        Long id = userService.findByEmail(principal.getName()).getId();
        Card card = cardService.findByUser_id(id);
        List<CartItemDTO> dtoList = new ArrayList<>();

        if (card != null) {
            List<CardItem> card_items = card.getCardItems();
            log.error(card_items.toString());
            int price;
            for (CardItem cardItem : card_items) {
                price = cardItem.getItem().getPrice() * cardItem.getCount();
                dtoList.add(
                        CartItemDTO.builder()
                                .itemCardId(cardItem.getId())
                                .title(cardItem.getItem().getTitle())
                                .image(cardItem.getItem().getImage())
                                .price(Long.valueOf(price))
                                .count(Long.valueOf(cardItem.getCount()))
                                .build()
                );
            }
            log.error(dtoList.toString());
            model.addAttribute("card", card);

        } else {
            log.error("card is empty");
        }
        ShopController.cardCount(model, principal, userService);

        model.addAttribute("items", dtoList);


        return "cart";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/add")
    public String addToCart(Model model, @ModelAttribute AddingItemDto dto,
                            Principal principal, HttpServletRequest request) {
        log.error(String.valueOf(dto));
        if (principal == null) {
            throw new RuntimeException("You are not authorize");
        }
        CardItem cardItem = new CardItem();
        Card card = userService.findByEmail(principal.getName()).getCard();
        Item item = itemService.findById(dto.getId());
        cardItem.setItem(item);
        cardItem.setCard(card);
        cardItem.setCount(dto.getCount().intValue());

        int sum = (int) card.getPrice();
        int count = card.getCardItems().size();

        CardItem cardItemDB = cardService.findByCardAndItem(card.getId(), item.getId());
        if (cardItemDB == null) {
            log.error("товар добавлен");
            card.setCount(count + 1);
            log.error(String.valueOf(count + 1));
            card.setPrice(sum + dto.getCount() * item.getPrice());
            cardService.saveItem(cardItem);
            cardService.updateCard(card);

        } else {
            cardItemDB.setCount((int) (cardItemDB.getCount() + dto.getCount()));
            cardService.saveItem(cardItemDB);
            card.setPrice(sum + dto.getCount() * item.getPrice());
            cardService.updateCard(card);
//
        }

        return getPreviousPageByRequest(request).orElse("/");


    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/buy")
    public String buyItems(Model model, Principal principal) {
        model.addAttribute("cart", userService.findByEmail(principal.getName()).getCard());
        model.addAttribute("purchaseData", new PurchaseDTO());
        return "checkout";
    }

    @PreAuthorize("isAuthenticated()")
    @RequestMapping(value = "/order", method = RequestMethod.POST)
    public String newOrder(Model model, Principal principal, @ModelAttribute PurchaseDTO dto, RedirectAttributes rm) throws MessagingException, IOException {
        User user = userService.findByEmail(principal.getName());
        if (principal != null) {
            Card card = userService.findByEmail(user.getEmail()).getCard();
            Purchase purchase = new Purchase();
            purchase.setUser(user);
            purchase.setUser_id(user.getId());
            LocalDate date = LocalDate.now();
            Long orderNumber = orderNumbGen(user.getId());

            purchase.setOrder_number(orderNumber);

            purchase.setDate(Date.valueOf(date));
            purchase.setAdress(dto.getCountry() + " " + dto.getAddress());
            purchase.setFio(dto.getFio());
            purchase.setSum((long) card.getPrice());
            if (dto.getComment() != null) {
                purchase.setComment(dto.getComment());
            }
            Purchase newPurchase = purchaseService.savePurchase(purchase);
            log.error(newPurchase.getId().toString());
            log.error(newPurchase.getUser_id().toString());

            ArrayList<PurchaseItem> purchaseItems = new ArrayList<>();
            PurchaseItem newItem = null;
            if (card != null) {
                for (CardItem item : card.getCardItems()) {
                    newItem = new PurchaseItem();
                    newItem.setPurchase(newPurchase);
                    newItem.setItem(item.getItem());
                    newItem.setCount(item.getCount());
                    newItem.setPrice(item.getCount() * item.getItem().getPrice());
                    purchaseItemService.savePurchaseItem(newItem);
                }
            }
            cardService.deleteCard(card);
            sendMail(user.getEmail(),purchase.getSum().toString(),orderNumber.toString());
        } else {
            log.error("null user");
        }


        return "thanks";

    }

    public void sendMail( String email,String sum,String orderNum)
    {

        EmailDetails details= new EmailDetails();
        details.setRecipient(email);
        String msg= "Благодарим за заказ! \n " +
                "Сумма вашего заказа: "+ sum+" p\n"+
                "Реквизиты для оплаты: 65868647648648658686 \n"+
                "После оплаты вам необходимо отправить чек в ответ на это сообщение. \n";

        details.setMsgBody(msg);
        String status
                = emailService.sendSimpleMail(details);

    }

    public Long orderNumbGen(Long userId) {
        Long orderNumber = userId * 10000000 + (int) (Math.random() * (9999999 - 1000000));
        if (purchaseService.findByOrderNumber(orderNumber) == null) {
            return orderNumber;
        } else {
            orderNumbGen(userId);
        }
        return orderNumber;
    }


    @PreAuthorize("isAuthenticated()")
    @RequestMapping(value = "/update", method = RequestMethod.POST, params = "plus")
    public String plusBtn(Model model, @ModelAttribute CartItemDTO dto,
                          Principal principal, HttpServletRequest request) {
        changeCount(dto, '+', principal);

        return getPreviousPageByRequest(request).orElse("/");

    }

    @PreAuthorize("isAuthenticated()")
    @RequestMapping(value = "/update", method = RequestMethod.POST, params = "minus")
    public String minusBtn(Model model, @ModelAttribute CartItemDTO dto,
                           Principal principal, HttpServletRequest request) {

        changeCount(dto, '-', principal);

        return getPreviousPageByRequest(request).orElse("/");


    }

    public void changeCount(CartItemDTO dto, char param, Principal principal) {

        Card card = userService.findByEmail(principal.getName()).getCard();
        CardItem cardItem = itemCartService.findById(dto.getItemCardId());
        if (param == '+') {
            cardItem.setCount(cardItem.getCount() + 1);
//            dto.setCount(dto.getCount()+1);
            changeCart(card);
            itemCartService.updateCount(cardItem);
        }else if (param == '-' && dto.getCount() > 1) {
            cardItem.setCount(cardItem.getCount() - 1);
            changeCart(card);
            itemCartService.updateCount(cardItem);
        } else if (param == '-' && dto.getCount() == 1) {
            deleteFromCart(cardItem);
            changeCart(card);
        }

    }

    public void changeCart(Card card) {

        List<CardItem> items = itemCartService.findAllByCartId(card.getId());
        card.setCount(card.getCardItems().size());
        long sum = 0;
        for (CardItem item : items) {
            long price = itemService.findById(item.getItem_id()).getPrice();
            log.error(String.valueOf(price));
            sum = sum + price * item.getCount();
        }
        card.setPrice(sum);
        cardService.updateCard(card);
    }


    public void deleteFromCart(CardItem cardItem) {
        itemCartService.deleteByItem(cardItem);
    }

    public Optional<String> getPreviousPageByRequest(HttpServletRequest request) {
        return Optional.ofNullable(request.getHeader("Referer")).map(requestUrl -> "redirect:" + requestUrl);
    }


}
