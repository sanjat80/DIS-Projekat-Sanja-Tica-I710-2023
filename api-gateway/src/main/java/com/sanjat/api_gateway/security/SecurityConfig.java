package com.sanjat.api_gateway.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

import com.sanjat.api_gateway.authentication.JwtAuthenticationFilter;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {
    /*
     * @Bean
     * 
     * @Order(1)
     * public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http,
     * JwtAuthenticationFilter jwtAuthFilter) {
     * return http
     * .csrf().disable()
     * .addFilterAt(jwtAuthFilter, SecurityWebFiltersOrder.AUTHENTICATION)
     * .authorizeExchange()
     * .pathMatchers("/auth/login", "/auth/register", "/user-service/auth/login",
     * "/user-service/auth/register", "/user-service/auth/register/professor")
     * .permitAll()
     * // ... ostale rute
     * .anyExchange().authenticated()
     * .and()
     * .httpBasic().disable() // Ako koristiš samo JWT, možeš isključiti basic auth
     * .build();
     * }
     */
    @Bean
    @Order(1)
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http,
            JwtAuthenticationFilter jwtAuthFilter) {
        return http
                .csrf(csrf -> csrf.disable())
                .addFilterAt(jwtAuthFilter, SecurityWebFiltersOrder.AUTHENTICATION)
                .authorizeExchange(exchanges -> exchanges
                        .pathMatchers(
                                "/auth/login",
                                "/auth/register",
                                "/user-service/auth/login",
                                "/user-service/auth/register",
                                "/user-service/auth/register/professor")
                        .permitAll()
                        .pathMatchers(HttpMethod.POST, "/auth/register/professor").hasRole("ADMIN")
                        .pathMatchers(HttpMethod.GET, "/courses").permitAll()
                        .pathMatchers(HttpMethod.POST, "/courses").hasAnyRole("ADMIN", "PROFESSOR")
                        .pathMatchers(HttpMethod.PUT, "/courses/*").hasAnyRole("ADMIN", "PROFESSOR")
                        .pathMatchers(HttpMethod.DELETE, "/courses/*").hasAnyRole("ADMIN", "PROFESSOR")
                        .pathMatchers(HttpMethod.POST, "/enrollments/apply").hasAnyRole("ADMIN", "PROFESSOR", "STUDENT")
                        .pathMatchers(HttpMethod.POST, "/enrollments/enroll/*").hasAnyRole("ADMIN", "PROFESSOR")
                        .pathMatchers(HttpMethod.GET, "/enrollments").hasAnyRole("ADMIN", "PROFESSOR")
                        .pathMatchers(HttpMethod.GET, "/enrollments/applications").hasAnyRole("ADMIN", "PROFESSOR")
                        .pathMatchers(HttpMethod.GET, "/enrollments/student/{studentId}")
                        .hasAnyRole("ADMIN", "PROFESSOR", "STUDENT")
                        .pathMatchers(HttpMethod.DELETE, "/enrollments/*").hasAnyRole("ADMIN", "PROFESSOR")
                        .pathMatchers(HttpMethod.POST, "/grades").hasAnyRole("ADMIN", "PROFESSOR")
                        .pathMatchers(HttpMethod.DELETE, "/grades/*").hasAnyRole("ADMIN", "PROFESSOR")
                        .pathMatchers(HttpMethod.GET, "/grades").hasAnyRole("ADMIN", "PROFESSOR")
                        .pathMatchers(HttpMethod.PUT, "/grades/*").hasAnyRole("ADMIN", "PROFESSOR")
                        .anyExchange().authenticated())
                .httpBasic(httpBasic -> httpBasic.disable())
                .build();
    }
}
