package com.example.dip.service.Interface;

import com.example.dip.entity.Card;
import com.example.dip.entity.Purchase;
import com.example.dip.entity.Role;
import com.example.dip.entity.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService extends UserDetailsService {

      User findByEmail(String email);
      User saveUser(User user);
      List<User> findAll();
      Card addCardToUser(User user);


}
