package com.example.dip.service;


import com.example.dip.entity.Category;
import com.example.dip.entity.Item;
import com.example.dip.repository.CategoryRepository;
import com.example.dip.repository.ItemRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional

// откат всех записей к предыдущему значению, если любая из операций в этом методе завершится неудачей,
// а также повторно бросит оригинальное исключение.
// Это значит, что если добавление одного из людей завершится ошибкой,
// то ни один из людей в итоге не добавится в таблицу BOOKINGS.
public class ItemService {

    private final ItemRepository repository;

    public ItemService(ItemRepository repository) {
        this.repository = repository;
    }

    public Item findById(Long id){
        return repository.findById(id).get();
    }

    public Item findByTitle(String title){
        return repository.findByTitle(title);
    }

    public Category getCategory(Long id){

        return repository.findById(id).get().getCategory();
    }

    public Item add(Item item){
        return repository.save(item);
    }

    public void deleteById(Long id){
        repository.deleteById(id);
    }

    public Category findCategoryByID(Long id){

        return repository.findCategoryById(id);

    }

    public List<Item> findAll() {
        return repository.findAll();
    }

    public void save(Item item) {
        repository.save(item);
    }
    public int min(){
        return repository.findMinimum();
    }


    public int max(){
        return repository.findMaximum();
    }
    public List<Item> findByRange(int min,int max){
        return repository.findItemsByRange(min,max);
    }

}
