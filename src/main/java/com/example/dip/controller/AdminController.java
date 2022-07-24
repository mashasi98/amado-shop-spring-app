package com.example.dip.controller;

import com.example.dip.entity.*;
import com.example.dip.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
@Slf4j
public class AdminController {

    private final UserServiceImpl userService;
    private final BrandService brandService;
    private final ItemService itemService;
    private final PurchaseService purchaseService;
    private final CategoryService categoryService;


    @PreAuthorize("isAuthenticated()")
    @GetMapping("/")
    //________USER Block__________
    public String main(Model model) {
        getUserList(model);
        model.addAttribute("user", new User());
        return "adminMain";
    }

    //
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/find")
    public String findUser(Model model, @ModelAttribute User user) {
        User clients = userService.findByEmail(user.getEmail());
        model.addAttribute("clients", clients);
        model.addAttribute("user", new User());
        return "adminMain";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/user/delete/{id}")
    public String deleteUser(Model model, @PathVariable Integer id) {
        userService.deleteById(Long.valueOf(id));
        getUserList(model);
        model.addAttribute("user", new User());
        return "adminMain";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/user/change/{id}")
    public String editUser(Model model, @PathVariable Integer id) {
        model.addAttribute("atribUser", true);
//        getCategoryList(model);
        User user = userService.findById(Long.valueOf(id));
        model.addAttribute("new_user", user);
        return "editUser";
    }

    public void getUserList(Model model) {
        List<User> clients = userService.findAll();
        model.addAttribute("clients", clients);
    }

    public String editUserAtr(Model model, User user) {
        model.addAttribute("atribUser", true);
        model.addAttribute("new_user", user);
        return "editUser";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/user/save")
    public String saveUser(Model model, @ModelAttribute User user) {
        User userDb = userService.findById(user.getId());
        if (userDb != null
                && user.getEmail() != null
                && user.getPassword() != null
                && user.getName() != null) {
            userService.saveUser(user);
            getUserList(model);
            model.addAttribute("user", new User());
            return "adminMain";
        }
        model.addAttribute("errUser", true);
        return editUserAtr(model, user);
    }


    //________Purchase Block_________
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/purchase")
    public String purchase(Model model) {
        List<Purchase> purchases = purchaseService.findAll();
        model.addAttribute("purchases", purchases);
        model.addAttribute("somePurchase", new Purchase());
        return "adminPurchases";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/findPurchase")
    public String findPurchase(Model model, @ModelAttribute Purchase purchase) {
        model.addAttribute("purchases", purchaseService.findByOrderNumber(purchase.getOrder_number()));
        model.addAttribute("somePurchase", new Purchase());
        return "adminPurchases";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/purchase/details/{id}")
    public String purchase(Model model, @PathVariable Integer id) {
        Purchase purchase = purchaseService.findById(Long.valueOf(id));
        if (purchase != null) {
            List<PurchaseItem> items = purchase.getPurchaseItems();
            model.addAttribute("purchase", purchase);
            model.addAttribute("items", items);
            return "adminPurchasesDetails";
        }
        return "adminPurchasesDetails";
    }


    //_______Category Block________
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/category")
    public String category(Model model) {
        getCategoryList(model);
        return "adminCategory";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/category/change/{id}")
    public String editCategory(Model model, @PathVariable Integer id) {
        model.addAttribute("atribCategory", true);
//        getCategoryList(model);
        Category category = categoryService.findById(Long.valueOf(id));
        model.addAttribute("new_category", category);
        return "editCategory";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/category/new")
    public String newCategory(Model model) {
        return editCategoryAtr(model);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/category/delete/{id}")
    public String deleteCategory(Model model, @PathVariable Integer id) {
        categoryService.deleteById(Long.valueOf(id));
        getCategoryList(model);
        return "adminCategory";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/category/save")
    public String saveCategory(Model model, @ModelAttribute Category category) {
        Category categoryDB = categoryService.findByTitle(category.getTitle());
        if (categoryDB == null && category.getTitle() != null) {
            categoryService.save(category);
            getCategoryList(model);
            model.addAttribute("errCategory", true);
            return "adminCategory";
        }
        model.addAttribute("errCategory", true);
        return editCategoryAtr(model);
    }

    public void getCategoryList(Model model) {
        List<Category> categories = categoryService.findAll();
        model.addAttribute("categories", categories);
    }

    public String editCategoryAtr(Model model) {
        model.addAttribute("atribCategory", true);
        model.addAttribute("new_category", new Category());
        return "editCategory";
    }
    //_________items________

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/categoryItem/{id}")
    public String allItemByCategory(Model model, @PathVariable Integer id) {
        Category category = categoryService.findById(Long.valueOf(id));
        List<Item> items = category.getItems();
        if (!items.isEmpty()) {
            model.addAttribute("items", items);
        }
        model.addAttribute("category", category);
        return "adminItem";
    }


    @PreAuthorize("isAuthenticated()")
    @GetMapping("/item/delete/{id}")
    public String deleteItem(Model model, @PathVariable Integer id) {
        Integer categoryId = Math.toIntExact(itemService.findById(id.longValue()).getCategory_id());
        itemService.deleteById(Long.valueOf(id));
        return allItemByCategory(model, categoryId);
    }


    @PreAuthorize("isAuthenticated()")
    @GetMapping("/item/change/{id}")
    public String changeItem(Model model, @PathVariable Integer id) {
        Item item = itemService.findById(id.longValue());
        model.addAttribute("brands", brandService.findAll());
        model.addAttribute("categories", categoryService.findAll());
        model.addAttribute("atribItem", true);
        model.addAttribute("new_item", item);
        return "editItem";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/item/new")
    public String newItem(Model model) {
        model.addAttribute("brands", brandService.findAll());
        model.addAttribute("categories", categoryService.findAll());
        model.addAttribute("atribItem", true);
        model.addAttribute("new_item", new Item());
        return "editItem";
    }


    @PreAuthorize("isAuthenticated()")
    @PostMapping("/item/save")
    public String itemCategory(Model model, @ModelAttribute Item item) {
        Category category = categoryService.findById(item.getCategory_id());
        Brand brand = brandService.findById(item.getBrand_id());

        item.setCategory(category);
        item.setBrand(brand);
        itemService.save(item);

        return allItemByCategory(model, Math.toIntExact(category.getId()));
    }


}
