package com.example.springdoc.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SpringDocSecurityConfig {

    @Bean
    public SecurityFilterChain getSecurityFilter(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authRequest->authRequest
                        //Only admins can delete
                        .requestMatchers(HttpMethod.DELETE,"/api/*/customers/**").hasRole( "ADMIN")
                        // Need authentication to check customers
                        .requestMatchers(HttpMethod.GET,"/api/*/customers/**").authenticated()
                        //anyone can create customer
                        .anyRequest().permitAll()
                )
                .build();
    }


    @Bean
    public UserDetailsService userDetailsService() {
        UserDetails user = User.builder()
                .username("user")
                .password("password")
                .roles("USER")
                .build();
        UserDetails admin = User.builder()
                .username("admin")
                .password("password")
                .roles("ADMIN")
                .build();
        return new InMemoryUserDetailsManager(user,admin);
    }
}
