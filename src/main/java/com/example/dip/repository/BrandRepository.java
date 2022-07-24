package com.example.dip.repository;


import com.example.dip.entity.Brand;
import com.example.dip.entity.Category;
import com.example.dip.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface BrandRepository extends JpaRepository<Brand,Long> {


    Brand findByTitle(String title);

//    @Query("select b from Category c where c.id = ?1")
//    Category findCategoryById(Long id);



}
