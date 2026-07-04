package com.ca.cafe_uni.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        // Rutas públicas — cualquiera puede acceder
                        .requestMatchers("/", "/menu", "/resenas/**", "/images/**", "/css/**", "/js/**").permitAll()
                        // Solo admin puede acceder a la gestión interna
                        .requestMatchers("/gestion-interna/**").hasRole("ADMIN")
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/gestion-interna/login")
                        .loginProcessingUrl("/gestion-interna/login")
                        .defaultSuccessUrl("/gestion-interna/dashboard", true)
                        .failureUrl("/gestion-interna/login?error=true")
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/gestion-interna/logout")
                        .logoutSuccessUrl("/gestion-interna/login")
                        .permitAll()
                );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService(PasswordEncoder encoder) {
        var admin = User.builder()
                .username("admin")
                .password(encoder.encode("Zack123@"))
                .roles("ADMIN")
                .build();

        return new InMemoryUserDetailsManager(admin);
    }
}