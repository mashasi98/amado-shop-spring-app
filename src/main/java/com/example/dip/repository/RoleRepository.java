package com.example.dip.repository;

import com.example.dip.entity.Role;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role,Long> {

    Role findByTitle(String title);

    @Query("select r from Role r where r.id = :id")
    Optional<Role> findById(@Param("id")  Long id);
}
