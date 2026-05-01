package com.codegnan.shopnest.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.codegnan.shopnest.Service.Impl.CustomUserDetailsService;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final CustomUserDetailsService customUserDetailsService;
    private final PasswordEncoder passwordEncoder;

    public SecurityConfig(CustomUserDetailsService customUserDetailsService,
                          PasswordEncoder passwordEncoder) {
        this.customUserDetailsService = customUserDetailsService;
        this.passwordEncoder = passwordEncoder;
    }

    // -------------------------------------------------------
    // AUTHENTICATION MANAGER
    // directly wires UserDetailsService + PasswordEncoder
    // no DaoAuthenticationProvider needed
    // -------------------------------------------------------
    @Bean
    public AuthenticationManager authenticationManager(
            HttpSecurity http) throws Exception {

        AuthenticationManagerBuilder builder =
                http.getSharedObject(AuthenticationManagerBuilder.class);

        builder
            .userDetailsService(customUserDetailsService)
            .passwordEncoder(passwordEncoder);

        return builder.build();
    }

    // -------------------------------------------------------
    // SECURITY FILTER CHAIN
    // -------------------------------------------------------
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http)
            throws Exception {

        // wire authentication first
        AuthenticationManager authManager = authenticationManager(http);

        http
            .authenticationManager(authManager)

            .authorizeHttpRequests(auth -> auth
                .requestMatchers(
                        "/css/**",
                        "/js/**",
                        "/images/**",
                        "/webjars/**",
                        "/error", "/error/**" 
                ).permitAll()
                .requestMatchers(
                        "/",
                        "/index",
                        "/category/**",
                        "/product/**",
                        "/search",
                        "/register",
                        "/login"
                ).permitAll()
                .requestMatchers("/checkout/**").authenticated()
                .requestMatchers("/admin/**")
                        .hasRole("ADMIN")
                .requestMatchers("/cart/**")
                        .authenticated()
                .anyRequest().authenticated()
            )

            .formLogin(form -> form
                .loginPage("/login")
                .loginProcessingUrl("/login")
                .usernameParameter("username")
                .passwordParameter("password")
                .defaultSuccessUrl("/", true)
                .failureUrl("/login?error=true")
                .permitAll()
            )

            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout=true")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
                .permitAll()
                
            );

        return http.build();
    }
}