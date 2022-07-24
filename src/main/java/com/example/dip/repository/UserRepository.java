package com.example.dip.repository;


import com.example.dip.entity.Card;
import com.example.dip.entity.Category;
import com.example.dip.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {

    User findByEmail(String email);



}
