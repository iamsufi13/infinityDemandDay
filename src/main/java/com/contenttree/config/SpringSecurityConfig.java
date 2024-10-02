package com.contenttree.config;

import com.contenttree.security.JwtAuthenticationFilter;
import com.contenttree.security.JwtHelper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
//@EnableWebSecurity/
public class SpringSecurityConfig {

    private final JwtHelper jwtHelper; // Ensure this is defined and injected
    private final UserDetailsService userDetailsService; // Ensure this is defined and injected
    private final AuthenticationEntryPoint point; // Ensure this is defined and injected

    public SpringSecurityConfig(JwtHelper jwtHelper, UserDetailsService userDetailsService, AuthenticationEntryPoint point) {
        this.jwtHelper = jwtHelper;
        this.userDetailsService = userDetailsService;
        this.point = point;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
                .authorizeRequests(authorizeRequests -> authorizeRequests
                        .requestMatchers("/admin").hasAnyRole("SUPERADMIN", "ADMIN", "EDITOR")
                        .requestMatchers("/api/login", "/api/register", "/admin/register", "/admin/login").permitAll()
                        .anyRequest().authenticated()
                )
                .exceptionHandling(ex -> ex.authenticationEntryPoint(point))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class); // Ensure proper filter order

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter(jwtHelper, userDetailsService);
    }
}
