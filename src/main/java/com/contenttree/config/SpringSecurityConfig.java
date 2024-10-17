package com.contenttree.config;

import com.contenttree.security.JwtAuthenticationFilter;
import com.contenttree.security.JwtHelper;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@Slf4j
@EnableWebSecurity
public class SpringSecurityConfig {

    private final JwtHelper jwtHelper;
    private final UserDetailsService userDetailsService;
    private final AuthenticationEntryPoint point;

    public SpringSecurityConfig(JwtHelper jwtHelper, UserDetailsService userDetailsService, AuthenticationEntryPoint point) {
        this.jwtHelper = jwtHelper;
        this.userDetailsService = userDetailsService;
        this.point = point;
    }

    @Bean
    @Order(1)
    public SecurityFilterChain adminSecurityFilterChain(HttpSecurity http) throws Exception {
        http
//                .securityMatcher(AntPathRequestMatcher.antMatcher("/admin/**"))
        .csrf(csrf -> csrf.disable())
                .cors(c -> c.disable())
                .authorizeRequests(authorizeRequests -> authorizeRequests
                        .requestMatchers(AntPathRequestMatcher.antMatcher("/admin/**")).hasAnyAuthority("SUPERADMIN", "EDITOR", "ADMIN")
                        .requestMatchers("/api/login", "/api/register","/api/category/**", "/api/vendor/login","/login/admin","/api/user/register","/api/user/login","/api/user/confirm-account","/api/home").permitAll()
                        .requestMatchers("/error").permitAll()
                        .anyRequest().authenticated()
                )
//                .oauth2Login(Customizer.withDefaults())
//                .formLogin(Customizer.withDefaults())
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint((request, response, authException) -> {
//                            Logger logger = null;
//                            logger.error("Unauthorized error: {}", authException.getMessage());
                            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
                        })
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
    @Bean
    @Order(2)
    public SecurityFilterChain vendorSecurityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.disable())
                .authorizeRequests(authorizeRequests -> authorizeRequests
                        .requestMatchers("/api/login","/api/vendor/login", "/api/register","login/admin").permitAll()
                        .anyRequest().authenticated()
                )
                .exceptionHandling(ex -> ex
                                .authenticationEntryPoint((request, response, authException) -> {
                                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
                                })
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
    @Bean
    @Order(3)
    public SecurityFilterChain userSecurityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.disable())
                .authorizeRequests(authorizeRequests -> authorizeRequests
                        .requestMatchers("/api/login", "/api/register","login/admin","api/user/register","/api/user/login","/confirm-account","/confirm-account/","/confirm-account/**","/api/user/confirm-account","/api/user/confirm-account/**").permitAll()
                        .requestMatchers("/error/**","/error","/error/").permitAll()
                        .anyRequest().authenticated()
                )
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint((request, response, authException) -> {
                            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
                        })
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

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
