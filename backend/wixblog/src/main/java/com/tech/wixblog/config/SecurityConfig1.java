//package com.tech.wixblog.config;
//
//import com.tech.wixblog.services.UserService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.http.HttpMethod;
//import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.http.SessionCreationPolicy;
//import org.springframework.security.web.SecurityFilterChain;
//import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
//import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
//
//@Configuration
//@EnableWebSecurity
//@EnableMethodSecurity(prePostEnabled = true)
//@RequiredArgsConstructor
//public class SecurityConfig1 {
//    private final UserService userService;
//
//    @Bean
//    public SecurityFilterChain filterChain (HttpSecurity http) throws Exception {
//        http
//                // REMOVED CORS configuration - handled by WebConfig
//                .csrf(csrf -> csrf.disable())
//                .sessionManagement(session -> session
//                                           .sessionCreationPolicy(SessionCreationPolicy.ALWAYS)
//                                           .maximumSessions(1)
//                                           .expiredUrl("http://localhost:4200/login")
//                                  )
//                .authorizeHttpRequests(authz -> authz
//                                               .requestMatchers(
//                                                       "/swagger-ui.html",
//                                                       "/swagger-ui/**",
//                                                       "/v3/api-docs/**",
//                                                       "/swagger-resources/**",
//                                                       "/swagger-resources",
//                                                       "/configuration/ui",
//                                                       "/configuration/security",
//                                                       "/webjars/**",
//                                                       "/favicon.ico",
//                                                       "/error",
//                                                       "/api/debug/**"  // Added debug endpoints
//                                                               ).permitAll()
//                                               // ===== AUTH ENDPOINTS =====
//                                               .requestMatchers("/auth/**").permitAll()
//                                               .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
//                                               .requestMatchers(
//                                                       "/oauth2/**",
//                                                       "/login/oauth2/**",
//                                                       "/login/**",
//                                                       "/auth/google-login",
//                                                       "auth/user"
//                                                               ).permitAll()
//
//                                               // ===== PUBLIC API ENDPOINTS =====
//                                               .requestMatchers(HttpMethod.GET,
//                                                                "/api/posts/**", "/api/posts/*/comments",
//                                                                "/api/posts/*/likes/*",
//                                                                "/api/posts/*/views/count",
//                                                                "/api/users/search",
//                                                                "/api/users/top-writers"
//                                                               ).permitAll()
//                                               .requestMatchers(HttpMethod.DELETE, "api/posts" +
//                                                       "/*/comments/*").permitAll()
//                                               .requestMatchers(HttpMethod.DELETE,
//                                                                "api/posts/*/likes").permitAll()
//                                               // ===== USER ENDPOINTS =====
//                                               .requestMatchers(HttpMethod.POST, "/api/posts/**").hasAnyRole("USER",
//                                                                                                             "ADMIN")
//                                               .requestMatchers(HttpMethod.PUT, "/api/posts/**").hasAnyRole("USER",
//                                                                                                            "ADMIN")
//                                               .requestMatchers(HttpMethod.DELETE, "/api/posts/**").hasAnyRole(
//                                                       "USER,ADMIN")
//                                               .requestMatchers("/api/posts/*/likes/**").hasAnyRole("USER", "ADMIN")
//                                               .requestMatchers("/api/posts/*/comments/**").hasAnyRole("USER",
//                                                                                                              "ADMIN")
//                                               .requestMatchers("/api/posts/my-posts/**").hasAnyRole("USER", "ADMIN")
//                                               .requestMatchers("/api/posts/*/views/has-viewed").hasAnyRole(
//                                                       "USER, ",
//                                                       "ADMIN")
//                                               .requestMatchers("/api/users/my-profile").hasAnyRole("USER", "ADMIN")
//                                               .requestMatchers("/api/users/profile").hasAnyRole("USER", "ADMIN")
//                                               .requestMatchers("/api/users/settings").hasAnyRole("USER", "ADMIN").
//                                               requestMatchers("/api/users/update-user-stats").hasAnyRole("USER", "ADMIN")
//                                               // ===== ADMIN ENDPOINTS =====
//                                               .requestMatchers("/api/admin/**").hasRole("ADMIN")
//                                               .requestMatchers(HttpMethod.GET, "/api/posts" +
//                                                       "/*/views").hasRole(
//                                                       "ADMIN")
//                                               .anyRequest().authenticated()
//                                      )
//                .oauth2Login(oauth2 -> oauth2
//                                     .userInfoEndpoint(userInfo -> userInfo
//                                                               .userService(userService)
//                                                      )
//                                     .successHandler((request, response, authentication) -> {
//                                         // Check if request came from Swagger UI
//                                         String referer = request.getHeader("Referer");
//                                         if (referer != null && referer.contains("/swagger-ui")) {
//                                             response.sendRedirect("/swagger-ui/index.html");
//                                         } else {
//                                             response.sendRedirect("http://localhost:4200/");
//                                         }
//                                     })
//                            )
//                .logout(logout -> logout
//                        .logoutSuccessUrl("http://localhost:4200")
//                        .invalidateHttpSession(true)
//                        .deleteCookies("JSESSIONID")
//                       );
//        return http.build();
//    }
//
////    @Bean
////    public AuthenticationSuccessHandler authenticationSuccessHandler () {
////        return new SimpleUrlAuthenticationSuccessHandler("http://localhost:4200");
////    }
//}