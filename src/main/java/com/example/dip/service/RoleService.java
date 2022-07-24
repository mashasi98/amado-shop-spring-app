package com.example.dip.service;

import com.example.dip.entity.Role;
import com.example.dip.repository.RoleRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RoleService {
    final private RoleRepository repository;

    public RoleService(RoleRepository repository) {
        this.repository = repository;
    }

    public Role add(Role role){
        return repository.save(role);
    }
    public Role findByTitle(String title){
        return repository.findByTitle(title);
    }

    public Optional<Role> findById(Long id){
        return repository.findById(id);
    }
}
