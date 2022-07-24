package com.example.dip.service;


import com.example.dip.entity.*;
import com.example.dip.repository.CardRepository;
import com.example.dip.repository.UserRepository;
import com.example.dip.service.Interface.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
// откат всех записей к предыдущему значению, если любая из операций в этом методе завершится неудачей,
// а также повторно бросит оригинальное исключение.
// Это значит, что если добавление одного из людей завершится ошибкой,
// то ни один из людей в итоге не добавится в таблицу BOOKINGS.
public class UserServiceImpl implements UserService  {

    private final UserRepository userRepository;
    private final  CardRepository cardRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
       Optional<User> user= Optional.ofNullable(userRepository.findByEmail(username));

        user.orElseThrow(()-> new UsernameNotFoundException("User not found with name "+username));
        return user.map(MyUserDetails::new).get();
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public User saveUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }


    @Override
    public Card addCardToUser(User user) {
        Card card = new Card();
        card.setCount(0);
        card.setPrice(0);
        card.setUser(userRepository.findByEmail(user.getEmail()));
        return cardRepository.save(card);
    }


    public User findById(Long id) {
       return userRepository.findById(id).get();
    }

    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }

}
