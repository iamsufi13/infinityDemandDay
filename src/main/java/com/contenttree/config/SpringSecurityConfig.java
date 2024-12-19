package com.contenttree.config;

import com.contenttree.security.JwtAuthenticationFilter;
import com.contenttree.security.JwtHelper;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.firewall.HttpFirewall;
import org.springframework.security.web.firewall.StrictHttpFirewall;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import static org.springframework.security.config.Customizer.withDefaults;

//@Configuration
//@Slf4j
//@EnableWebSecurity
//public class SpringSecurityConfig {
//
//    private final JwtHelper jwtHelper;
//    private final UserDetailsService userDetailsService;
//    private final AuthenticationEntryPoint point;
//
//    public SpringSecurityConfig(JwtHelper jwtHelper, UserDetailsService userDetailsService, AuthenticationEntryPoint point) {
//        this.jwtHelper = jwtHelper;
//        this.userDetailsService = userDetailsService;
//        this.point = point;
//    }
//
//    @Bean
//    public AuthenticationEntryPoint authenticationEntryPoint() {
//        return (request, response, authException) -> {
//            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
//        };
//    }
//
//    @Bean
//    @Order(1)
//    public SecurityFilterChain adminSecurityFilterChain(HttpSecurity http) throws Exception {
//        http
////                .securityMatcher(AntPathRequestMatcher.antMatcher("/admin/**"))
//                .csrf(AbstractHttpConfigurer::disable)
//                .cors(withDefaults())
//                .cors(AbstractHttpConfigurer::disable)
//                .authorizeRequests(authorizeRequests -> authorizeRequests
//                        .requestMatchers(AntPathRequestMatcher.antMatcher("/admin/**")).hasAnyAuthority("SUPERADMIN", "EDITOR", "ADMIN")
//                        .requestMatchers("/api/country","/uploads/**","/uploads/**","/api/user/view-pdf","/api/login", "/api/register","/api/category/**", "/api/vendor/login","/login/admin","/register/admin","/api/user/register","/api/user/login","/user/register","/api/user/confirm-account","/api/home","/api/vendor/login","/api/vendor/register","/api/vendor/register123","/error/**","/api/category").permitAll()
//                        .requestMatchers(HttpMethod.OPTIONS).permitAll()
//                        .requestMatchers("/error").permitAll()
//                        .anyRequest().authenticated()
//                )
//
////                .oauth2Login(Customizer.withDefaults())
////                .formLogin(Customizer.withDefaults())
//                .exceptionHandling(ex -> ex
//                                .authenticationEntryPoint((request, response, authException) -> {
////                            Logger logger = null;
////                            logger.error("Unauthorized error: {}", authException.getMessage());
//                                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
//                                })
//                )
//                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
//                .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
//
//        return http.build();
//    }
//    @Bean
//    @Order(2)
//    public SecurityFilterChain vendorSecurityFilterChain(HttpSecurity http) throws Exception {
//        http
//
//                .csrf(csrf-> csrf.disable())
////                .cors(withDefaults())
//                .cors(withDefaults())
//                .csrf(AbstractHttpConfigurer::disable)
//                .authorizeRequests(authorizeRequests -> authorizeRequests
//                        .requestMatchers("/api/user/registers","/api/login","/api/vendor/login", "/api/register","login/admin").permitAll()
//                        .requestMatchers(HttpMethod.OPTIONS).permitAll()
//                        .anyRequest().authenticated()
//                )
//                .exceptionHandling(ex -> ex
//                        .authenticationEntryPoint((request, response, authException) -> {
//                            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
//                        })
//                )
//                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
//                .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
//
//        return http.build();
//    }
//    @Bean
//    @Order(3)
//    public SecurityFilterChain userSecurityFilterChain(HttpSecurity http) throws Exception {
//        http
//                .csrf(csrf -> csrf.disable())
//                .cors(withDefaults())
//                .csrf(AbstractHttpConfigurer::disable)
////                .cors(withDefaults())
//                .authorizeRequests(authorizeRequests -> authorizeRequests
//                        .requestMatchers("/api/login", "/api/register","login/admin","api/user/register","/api/user/login","/confirm-account","/confirm-account/","/confirm-account/**","/api/user/confirm-account","/api/user/confirm-account/**").permitAll()
//                        .requestMatchers("/error/**","/error","/error/").permitAll()
//                        .anyRequest().authenticated()
//                )
//                .exceptionHandling(ex -> ex
//                        .authenticationEntryPoint((request, response, authException) -> {
//                            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
//                        })
//                )
//                .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
//
//        return http.build();
//    }
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http
//                .headers(headers -> headers
//                        .frameOptions(HeadersConfigurer.FrameOptionsConfig::disable)
//                )
//                .authorizeHttpRequests(auth -> auth
//                        .anyRequest().permitAll()
//                );
//
//        return http.build();
//    }
//
//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
//
//    @Bean
//    public JwtAuthenticationFilter jwtAuthenticationFilter() {
//        return new JwtAuthenticationFilter(jwtHelper, userDetailsService);
//    }
//    @Bean
//    public CorsConfigurationSource corsConfigurationSource() {
//        CorsConfiguration config = new CorsConfiguration();
//        config.setAllowedOrigins(List.of("*"));
//        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
//        config.setAllowedHeaders(List.of("*"));
//        config.setAllowCredentials(false);
//
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        source.registerCorsConfiguration("/**", config);
//        return source;
//    }
//
//
//}
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
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of("*"));  // Allow all origins, refine for production
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(false);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return source;
    }

    @Bean
    public SecurityFilterChain adminSecurityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(withDefaults())  // Use global CORS config
                .authorizeRequests(authorizeRequests -> authorizeRequests
                        .requestMatchers("/api/user/get-whitepaper","/api/user/get-whitepaper/**","/api/country","/uploads/**","/uploads/**","/api/user/view-pdf","/api/login", "/api/register","/api/category/**","/api/user/solution-sets-homepage", "/api/vendor/login","/login/admin","/register/admin","/api/user/register","/api/user/login","/user/register","/api/user/confirm-account","/api/home","/api/vendor/login","/api/vendor/register","/api/vendor/register123","/error/**","/api/category","/api/user/download-pdf").permitAll()
                        .requestMatchers("/var/***","/uploads/**").permitAll()
                        .requestMatchers("/api/vendor/get-allwhitepapers","/uploads/**").permitAll()
                        .requestMatchers("/admin/**").hasAnyAuthority("SUPERADMIN", "EDITOR", "ADMIN")
                        .requestMatchers(HttpMethod.OPTIONS).permitAll()
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
    public SecurityFilterChain vendorSecurityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(withDefaults())  // Use global CORS config
                .authorizeRequests(authorizeRequests -> authorizeRequests
                        .requestMatchers("/api/country","/uploads/**","/uploads/**","/api/user/view-pdf","/api/login", "/api/register","/api/category/**", "/api/vendor/login","/login/admin","/register/admin","/api/user/register","/api/user/login","/user/register","/api/user/confirm-account","/api/home","/api/vendor/login","/api/vendor/register","/api/vendor/register123","/error/**","/api/category").permitAll()
                        .requestMatchers(HttpMethod.OPTIONS).permitAll()  // Allow OPTIONS
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
    public SecurityFilterChain userSecurityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .cors(withDefaults())  // Use global CORS config
                .authorizeRequests(authorizeRequests -> authorizeRequests
                        .requestMatchers("/api/login", "/api/register", "/confirm-account").permitAll()
                        .requestMatchers("/api/country","/uploads/**","/uploads/**","/api/user/view-pdf","/api/login", "/api/register","/api/category/**", "/api/vendor/login","/login/admin","/register/admin","/api/user/register","/api/user/login","/user/register","/api/user/confirm-account","/api/home","/api/vendor/login","/api/vendor/register","/api/vendor/register123","/error/**","/api/category").permitAll()
                        .requestMatchers(HttpMethod.OPTIONS).permitAll()  // Allow OPTIONS
                        .anyRequest().authenticated()
                )
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint((request, response, authException) -> {
                            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
                        })
                )
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

