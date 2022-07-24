package com.example.dip.service.custom;

import com.example.dip.entity.User;
import com.example.dip.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class CustomUserDetailService  implements UserDetailsService {
    
    
    private final UserRepository userRepository;

    public CustomUserDetailService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
      final User user = userRepository.findByEmail(email);
      if (user==null){
          throw new UsernameNotFoundException(email);
          
      }
//      boolean enabled = !user.isEnabled();
      UserDetails userDetails =
              org.springframework.security.core.userdetails.User
              .withUsername(user.getEmail())
              .password(user.getPassword())
                      //                      .disabled(enabled)
              .authorities("ROLE_USER")
              .roles("USER")
              .build();
      return userDetails;

    }
}
