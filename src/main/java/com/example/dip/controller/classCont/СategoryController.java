package com.example.dip.controller.classCont;


import com.example.dip.entity.Category;
import com.example.dip.entity.Item;
import com.example.dip.entity.User;
import com.google.gson.Gson;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.example.dip.service.CategoryService;

import java.util.List;

@RestController
@RequestMapping("/category")
public class СategoryController {

    private final CategoryService service;

    public СategoryController(CategoryService service) {
        this.service = service;
    }


    @PostMapping("/id")
    public Category findById(@RequestBody String id){
        return service.findById(Long.parseLong(id));
    }

    @PostMapping("/title")
    public Category findByTitle(@RequestBody String title){
        Category category=service.findByTitle(title.trim());
        System.out.println(category);
        return category;
    }

    @PostMapping("/items")
    public List<Item> getItems(@RequestBody String id){
        return service.getItems(Long.parseLong(id));
    }


    @PutMapping("/add")
    public ResponseEntity<Category> add(@RequestBody String userSrt){

        Gson gson = new Gson();
        System.out.println(userSrt);// Or use new GsonBuilder().create();
        Category category = gson.fromJson(userSrt, Category.class);

        System.out.println(category.getId());
        System.out.println(category.getTitle());

        if (category.getId() != null && category.getId() !=0){
            return new ResponseEntity("redundant param : id MUST be null", HttpStatus.NOT_ACCEPTABLE);
        }
        if (category.getTitle() == null || category.getTitle().trim().length() ==0){
            return new ResponseEntity("missrd param : title  ", HttpStatus.NOT_ACCEPTABLE);
        }

        if(service.findByTitle(category.getTitle().trim())!=null ){
            return new ResponseEntity("This category alredy exist", HttpStatus.NOT_ACCEPTABLE);
        }
        return ResponseEntity.ok(service.add(category));
    }


    @PutMapping("/update")
    public ResponseEntity<Category> update(@RequestBody String userSrt){

        Gson gson = new Gson(); // Or use new GsonBuilder().create();
        Category category = gson.fromJson(userSrt, Category.class);
        System.out.println(category.getId());

        if (category.getId() == null && category.getId() ==0){
            return new ResponseEntity("missrd param : id" , HttpStatus.NOT_ACCEPTABLE);
        }
        if (category.getTitle() == null || category.getTitle().trim().length() ==0){
            return new ResponseEntity("missrd param : title ", HttpStatus.NOT_ACCEPTABLE);
        }

        return ResponseEntity.ok(service.add(category));
    }

    @PostMapping ("/delete")
    public ResponseEntity delete(@RequestBody String id){

        try{
            service.deleteById(Long.parseLong(id));
        }catch (EmptyResultDataAccessException e){
            e.printStackTrace();
            return new ResponseEntity("id= "+ id + "not found",HttpStatus.NOT_ACCEPTABLE);
        }
        return new ResponseEntity(HttpStatus.OK);

    }


}
