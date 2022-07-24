package com.example.dip.service;


import com.example.dip.entity.Brand;
import com.example.dip.entity.Category;
import com.example.dip.entity.Item;
import com.example.dip.repository.BrandRepository;
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
public class BrandService {

    private final BrandRepository repository;

    public BrandService(BrandRepository repository) {
        this.repository = repository;
    }

    public Brand findById(Long id){
        return repository.findById(id).get();
    }

    public Brand findByTitle(String title){
        return repository.findByTitle(title);
    }


    public Brand add(Brand brand){
        return repository.save(brand);
    }

    public void deleteById(Long id){
        repository.deleteById(id);
    }

    public List<Brand> findAll() {
        return repository.findAll();
    }
}
