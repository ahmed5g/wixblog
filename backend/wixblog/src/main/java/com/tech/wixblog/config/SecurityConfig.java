package com.tech.wixblog.config;

import com.tech.wixblog.security.TokenAuthenticationFilter;
import com.tech.wixblog.security.oauth2.CustomAuthorizationRequestResolver;
import com.tech.wixblog.security.oauth2.HttpCookieOauth2AuthorizationRequestRepository;
import com.tech.wixblog.security.oauth2.OAuth2AuthenticationFailureHandler;
import com.tech.wixblog.security.oauth2.OAuth2AuthenticationSuccessHandler;
import com.tech.wixblog.security.oauth2.user.CustomOAuth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final static String OAUTH2_BASE_URI = "/oauth2/authorize";
    private final static String OAUTH2_REDIRECTION_ENDPOINT = "/oauth2/callback/*";
    private final CustomOAuth2UserService customOAuth2UserService;
    private final OAuth2AuthenticationFailureHandler oAuth2AuthenticationFailureHandler;
    private final OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;
    private final HttpCookieOauth2AuthorizationRequestRepository httpCookieOauth2AuthorizationRequestRepository;
    private final TokenAuthenticationFilter tokenAuthenticationFilter;
    private final ClientRegistrationRepository clientRegistrationRepository;

    @Bean
    public HttpCookieOauth2AuthorizationRequestRepository cookieOAuth2AuthorizationRequestRepository () {
        return new HttpCookieOauth2AuthorizationRequestRepository();
    }

    @Bean
    protected SecurityFilterChain configure (HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable);
        http.cors(cors -> cors.configurationSource(corsConfigurationSource()));
        http.sessionManagement(sessionManagement -> sessionManagement.sessionCreationPolicy(
                SessionCreationPolicy.STATELESS));
        http.authorizeHttpRequests(
                auth -> auth
                        .requestMatchers("/v3/api-docs/**", "/swagger-ui/**").permitAll()
                        .requestMatchers("/token/refresh/**").permitAll()
                        .requestMatchers("/api/v1/posts/published").permitAll()
                        .requestMatchers("/user/register").permitAll()
                        .requestMatchers("/auth/login").permitAll()
                        .requestMatchers("/", "/error").permitAll()
                        .requestMatchers("/auth/**", "/oauth2/**").permitAll()
                        .anyRequest().authenticated());
        http
                .oauth2Login(oauth2 -> oauth2
                                     .authorizationEndpoint(
                                             authorizationEndpointConfig -> authorizationEndpointConfig
                                                     .baseUri(OAUTH2_BASE_URI)
                                                     .authorizationRequestRepository(
                                                             httpCookieOauth2AuthorizationRequestRepository)
                                                     .authorizationRequestResolver(
                                                             new CustomAuthorizationRequestResolver(
                                                                     clientRegistrationRepository,
                                                                     OAUTH2_BASE_URI))
                                                           )
                                     .redirectionEndpoint(
                                             redirectionEndpointConfig -> redirectionEndpointConfig.baseUri(
                                                     OAUTH2_REDIRECTION_ENDPOINT))
                                     .userInfoEndpoint(
                                             userInfoEndpointConfig -> userInfoEndpointConfig.userService(
                                                     customOAuth2UserService))
                                     .successHandler(oAuth2AuthenticationSuccessHandler)
                                     .failureHandler(oAuth2AuthenticationFailureHandler)
                            );
        http.addFilterBefore(tokenAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        http.exceptionHandling(exceptions -> exceptions
                                       .authenticationEntryPoint(new HttpStatusEntryPoint(
                                               HttpStatus.UNAUTHORIZED))
                              );
        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager (AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource () {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of("http://localhost:4200"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
        config.setAllowedHeaders(
                List.of("Authorization", "Content-Type", "X-Requested-With", "Accept"));
        config.setAllowCredentials(true);
        config.setMaxAge(3600L);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

}