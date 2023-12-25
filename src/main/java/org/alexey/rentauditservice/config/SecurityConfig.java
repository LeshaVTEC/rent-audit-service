package org.alexey.rentauditservice.config;

import jakarta.servlet.http.HttpServletResponse;
import org.alexey.rentauditservice.controller.filter.JwtFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.http.HttpMethod.*;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtFilter filter) throws Exception  {
        // Enable CORS and disable CSRF
        http = http.cors().and().csrf().disable();

        // Set session management to stateless
        http = http
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and();

        // Set unauthorized requests exception handler
        http = http
                .exceptionHandling()
                .authenticationEntryPoint(
                        (request, response, ex) -> {
                            response.setStatus(
                                    HttpServletResponse.SC_UNAUTHORIZED
                            );
                        }
                )
                .accessDeniedHandler((request, response, ex) -> {
                    response.setStatus(
                            HttpServletResponse.SC_FORBIDDEN
                    );
                })
                .and();

        // Set permissions on endpoints
        http.authorizeHttpRequests(requests -> requests
                // Our public endpoints
//                .requestMatchers( "/realty/api/**").permitAll()
//                //Следующие два пример делают одно и тоже
                .requestMatchers(GET,"/audit").hasAnyRole("ADMIN") //Обрати внимание что тут нет префикса ROLE_
                .requestMatchers(GET,"/audit/{id}").hasAnyRole("ADMIN") //Обрати внимание что тут нет префикса ROLE_
                .requestMatchers(POST,"/audit").hasAnyRole("SYSTEM") //Обрати внимание что тут нет префикса ROLE_
//                .requestMatchers(GET,"/users").hasAnyAuthority("ROLE_ADMIN") //А тут есть
                .requestMatchers(GET,"/audit/**").authenticated()
//                // Our private endpoints
//                .anyRequest().authenticated()
        );

        // Add JWT token filter
        http.addFilterBefore(
                filter,
                UsernamePasswordAuthenticationFilter.class
        );

        return http.build();
    }
}