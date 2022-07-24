package com.example.dip.controller.classCont;


import com.example.dip.entity.Category;
import com.example.dip.entity.Item;
import com.example.dip.service.ItemService;
import com.google.gson.Gson;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/item")
public class ItemController {

    private final ItemService service;

    public ItemController(ItemService service) {
        this.service = service;
    }

    @PostMapping("/id")
    public Item findById(@RequestBody String id){
        return service.findById(Long.parseLong(id.trim()));
    }

    @PostMapping("/title")
    public Item findByTitle(@RequestBody String title){
        return service.findByTitle(title.trim());
    }

    @PostMapping("/category")
    public Long getCategoryTitle(@RequestBody String id){
        return service.getCategory(Long.parseLong(id.trim())).getId();
    }

    @PutMapping("/add")
    public ResponseEntity<Item> add(@RequestBody String userSrt){

        Gson gson = new Gson();
        System.out.println(userSrt);// Or use new GsonBuilder().create();
        Item item = gson.fromJson(userSrt, Item.class);

        Long cat_id=item.getCategory_id();
        System.out.println("getCategory_id "+ cat_id);
        Category category =service.findCategoryByID(cat_id);
        item.setCategory(category);

        if (item.getId() != null && item.getId() !=0){
            return new ResponseEntity("redundant param : id MUST be null", HttpStatus.NOT_ACCEPTABLE);
        }
        if (item.getTitle() == null || item.getTitle().trim().length() ==0){
            return new ResponseEntity("missrd param : title  ", HttpStatus.NOT_ACCEPTABLE);
        }
        if (category==null ){
            return new ResponseEntity("missrd param : category  ", HttpStatus.NOT_ACCEPTABLE);
        }

        if (  Integer. valueOf(item.getPrice())==null){
            return new ResponseEntity("missrd param : price ", HttpStatus.NOT_ACCEPTABLE);
        }

        if(service.findByTitle(item.getTitle().trim())!=null ){
            return new ResponseEntity("This item already exist", HttpStatus.NOT_ACCEPTABLE);
        }
        return ResponseEntity.ok(service.add(item));
    }


    @PutMapping("/update")
    public ResponseEntity<Item> update(@RequestBody String userSrt){

        Gson gson = new Gson(); // Or use new GsonBuilder().create();
        Item item = gson.fromJson(userSrt, Item.class);
        System.out.println(item.getId());
        Long cat_id=item.getCategory_id();
        System.out.println("getCategory_id "+ cat_id);
        Category category =service.getCategory(cat_id);
        item.setCategory(category);

        if (item.getId() == null && item.getId() ==0){
            return new ResponseEntity("missrd param : id" , HttpStatus.NOT_ACCEPTABLE);
        }
        if (item.getTitle() == null || item.getTitle().trim().length() ==0){
            return new ResponseEntity("missrd param : title  ", HttpStatus.NOT_ACCEPTABLE);
        }
        if (category==null ){
            return new ResponseEntity("missrd param : category  ", HttpStatus.NOT_ACCEPTABLE);
        }

        if (  Integer. valueOf(item.getPrice())==null){
            return new ResponseEntity("missrd param : price ", HttpStatus.NOT_ACCEPTABLE);
        }

        return ResponseEntity.ok(service.add(item));
    }
//
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
