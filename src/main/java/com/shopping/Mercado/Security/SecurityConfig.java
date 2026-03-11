package com.shopping.Mercado.Security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http) throws Exception {
        return http
                .cors(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session
                        .sessionCreationPolicy(
                                SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(requests -> requests

                        // Auth — public
                        .requestMatchers("/api/user/login",
                                "/api/user/register").permitAll()

                        // Products & Categories GET — public
                        .requestMatchers(HttpMethod.GET,
                                "/api/products/**",
                                "/api/categories/**").permitAll()

                        // Products — SELLER + ADMIN
                        .requestMatchers(HttpMethod.POST,
                                "/api/products/**")
                        .hasAnyRole("SELLER", "ADMIN")
                        .requestMatchers(HttpMethod.PATCH,
                                "/api/products/**")
                        .hasAnyRole("SELLER", "ADMIN")
                        .requestMatchers(HttpMethod.DELETE,
                                "/api/products/**")
                        .hasAnyRole("SELLER", "ADMIN")

                        // Categories — ADMIN only
                        .requestMatchers(HttpMethod.POST,
                                "/api/categories/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PATCH,
                                "/api/categories/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE,
                                "/api/categories/**").hasRole("ADMIN")

                        // Cart — CUSTOMER only
                        .requestMatchers("/api/cart/**").hasRole("CUSTOMER")

                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthFilter,
                        UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration auth) throws Exception {
        return auth.getAuthenticationManager();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}