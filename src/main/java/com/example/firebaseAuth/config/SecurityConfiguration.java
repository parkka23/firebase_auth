package com.example.firebaseAuth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import org.springframework.security.oauth2.jwt.JwtDecoders;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.stream.Collectors;

//
//@Configuration
//public class SecurityConfiguration {
//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        http
//                .csrf().disable()
//                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//                .and()
//                .authorizeRequests()
//                .antMatchers("/app/register").permitAll()// Allow unauthenticated access to /app/register
//                .anyRequest().authenticated();
//
//        http.oauth2ResourceServer().jwt();
//
//        return http.build();
//    }
//
//
//}

//@Configuration
//@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
//public class SecurityConfiguration {
//
//    @Bean
//    public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
////        http
////                .csrf().disable()
////                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
////                .authorizeRequests(authorizeRequests ->
////                        authorizeRequests
////                                .antMatchers("/app/register").permitAll()
//////                                .antMatchers("/app/test").anonymous()
////                                .anyRequest().authenticated()
////                )
////                .oauth2ResourceServer(oauth2 ->
////                        oauth2
////                                .jwt(jwt ->
////                                        jwt.decoder(jwtDecoder())
////                                )
////                );
//
//        http.csrf().disable();
//
//        http.oauth2ResourceServer()
//                .jwt();
//        return http.build();
//    }
//
//    public JwtAuthenticationConverter jwtAuthenticationConverter() {
//        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
//
//        converter.setJwtGrantedAuthoritiesConverter(jwt ->
//                Optional.ofNullable(jwt.getClaimAsStringList("custom_claims"))
//                        .stream()
//                        .flatMap(Collection::stream)
//                        .map(SimpleGrantedAuthority::new)
//                        .collect(Collectors.toList())
//        );
//
////        converter.setJwtGrantedAuthoritiesConverter(jwt ->
////                Optional.ofNullable(jwt.getClaimAsStringList("custom_claims"))
////                        .stream()
////                        .flatMap(claims -> claims.stream().map(SimpleGrantedAuthority::new))
////                        .collect(Collectors.toList())
////        );
//
////        converter.setJwtGrantedAuthoritiesConverter(jwt ->
////                jwt.getClaimAsStringList("custom_claims")
////                        .stream()
////                        .map(SimpleGrantedAuthority::new)
////                        .collect(Collectors.toList())
////        );
//
//
//        return converter;
//    }
//
//    @Bean
//    public JwtDecoder jwtDecoder() {
//        return JwtDecoders.fromOidcIssuerLocation("https://securetoken.google.com/spring-firebase-auth");
//    }
//}

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class SecurityConfiguration {

    @Bean
    public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests(authorizeRequests ->
                        authorizeRequests
                                .antMatchers("/app/register").permitAll()
                                .antMatchers("/admin/user-claims/**").permitAll()  // Allow anonymous access to /admin/user-claims/**
                                .anyRequest().authenticated()
                )
                .oauth2ResourceServer(oauth2 ->
                        oauth2
                                .jwt(jwt ->
                                        jwt.decoder(jwtDecoder())
                                                .jwtAuthenticationConverter(jwtAuthenticationConverter())
                                )
                );

        return http.build();
    }

    @Bean
    public JwtDecoder jwtDecoder() {
        return JwtDecoders.fromOidcIssuerLocation("https://securetoken.google.com/spring-firebase-auth");
    }

    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();

        converter.setJwtGrantedAuthoritiesConverter(jwt ->
                Optional.ofNullable(jwt.getClaimAsStringList("custom_claims"))
                        .stream()
                        .flatMap(Collection::stream)
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList())
        );

        return converter;
    }
}