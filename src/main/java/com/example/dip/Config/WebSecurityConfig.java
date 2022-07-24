package com.example.dip.Config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.PortMapperImpl;
import org.springframework.security.web.PortResolverImpl;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;

import java.util.Collections;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@Slf4j
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
//
    private final UserDetailsService userDetailsService;

    @Bean
    public PasswordEncoder getPasswordEncoder(){
        return NoOpPasswordEncoder.getInstance();
    }

    @Bean
    public AuthenticationSuccessHandler successHandler() {
        return new MyCustomLoginSuccessHandler("/home");
    }


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService);
//                .passwordEncoder(getPasswordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

            PortMapperImpl portMapper = new PortMapperImpl();
             portMapper.setPortMappings(Collections.singletonMap("8081", "8081"));
            PortResolverImpl portResolver = new PortResolverImpl();
            portResolver.setPortMapper(portMapper);
            LoginUrlAuthenticationEntryPoint entryPoint = new LoginUrlAuthenticationEntryPoint(
                    "/login");
            entryPoint.setPortMapper(portMapper);
            entryPoint.setPortResolver(portResolver);


            http
                .exceptionHandling()
                .authenticationEntryPoint(entryPoint)
                    .and()
                    .httpBasic().disable()
                    .csrf().disable()
                    .authorizeRequests()
                        .antMatchers("/admin/**").hasAnyAuthority( "ROLE_ADMIN")
                        .antMatchers("/user/**","/cart/**","/favorite/**").hasAnyAuthority("ROLE_USER", "ROLE_ADMIN")
                        .antMatchers("/**").permitAll()
                .and()
                    .formLogin()
                    .loginPage("/login")
                    .loginProcessingUrl("/perform-login")
                    .usernameParameter("user_name")
                    .passwordParameter("pass_name")
                    .successHandler(successHandler())
//                    .defaultSuccessUrl("/home")
                    .failureUrl("/login")
                    .permitAll()
                .and()
                    .rememberMe()
                    .userDetailsService(userDetailsService)
                .and()
                    .logout()
                    .logoutSuccessUrl("/home")
                    .deleteCookies("JSESSIONID")
                    .invalidateHttpSession(true);

    }


}