package com.example.dip.repository;


import com.example.dip.entity.Category;
import com.example.dip.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<Item,Long> {

    Item findByTitle(String title);

    @Query("select c from Category c where c.id = ?1")
    Category findCategoryById(Long id);

    @Query (value = "Select min(d.price) from item d", nativeQuery = true)
    int findMinimum();

    @Query (value = "Select max(d.price) from item d", nativeQuery = true)
    int findMaximum();

    @Query("SELECT i FROM Item i WHERE i.price>= :minim and i.price<= :maxim")
    List<Item> findItemsByRange(@Param("minim") int userStatus,
                                @Param("maxim") int userName);

}
