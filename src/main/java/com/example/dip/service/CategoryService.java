package com.example.dip.service;


import com.example.dip.entity.Category;
import com.example.dip.entity.Item;
import com.example.dip.entity.User;
import org.springframework.stereotype.Service;
import com.example.dip.repository.CategoryRepository;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional

// откат всех записей к предыдущему значению, если любая из операций в этом методе завершится неудачей,
// а также повторно бросит оригинальное исключение.
// Это значит, что если добавление одного из людей завершится ошибкой,
// то ни один из людей в итоге не добавится в таблицу BOOKINGS.
public class CategoryService {



    private final CategoryRepository repository;

    public CategoryService(CategoryRepository repository) {
        this.repository = repository;
    }

    public  List<Category>findAll(){

        return repository.findAll();
    }

    public Category findById(Long id){
        return repository.findById(id).get();
    }

    public Category add(Category category){
        return repository.save(category);
    }

    public void deleteById(Long id){repository.deleteById(id);}

    public List<Item> getItems(Long id){
        List<Item> list = repository.findById(id).get().getItems();
        return list;
    }

    public Category findByTitle(String title){
        return repository.findByTitle(title);
    }

    public void save(Category category) {
        repository.save(category);
    }
}
