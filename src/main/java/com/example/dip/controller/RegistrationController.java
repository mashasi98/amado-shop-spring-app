package com.example.dip.controller;


import com.example.dip.entity.Role;
import com.example.dip.entity.User;
import com.example.dip.service.Interface.UserService;
import com.example.dip.service.RoleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import java.lang.management.OperatingSystemMXBean;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;

@Controller
@Slf4j
@RequestMapping("/registration")
@RequiredArgsConstructor
public class RegistrationController {

    private static final String PASS_PATTERN = "(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}";

    private final RoleService roleService;
    private final UserService userService;

    @GetMapping
    public String main(Model model) {
        model.addAttribute("user", new User());
        return "/registration";
    }

    @PostMapping
    public String addUser(User user, Model model) {
        model.addAttribute("user", user);

        log.error(user.getEmail()+" "+ user.getName()+" "+user.getPassword().length());

        if (userService.findByEmail(user.getEmail())!=null){
            log.error("пользователь существует");
            model.addAttribute("regEmailError", true);
            return "/registration";
        }
        if( (user.getEmail()==null )||( user.getName() == null )|| (user.getPassword() == null )){
            model.addAttribute("regEmptyError", true);
            return "/registration";
        }

        if(user.getPassword().length()<7 ){
         model.addAttribute("regPassError", true);
            return "/registration";
        }

        user.setActive(true);
        Role role= roleService.findByTitle("ROLE_USER");
        log.error(String.valueOf(role));
        user.setRoles(Collections.singletonList(role));
        userService.saveUser(user);
        userService.addCardToUser(user);
        return "/login";
    }



}
