package com.sanjat.enrollment_service.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class ServiceAuthFilter extends OncePerRequestFilter {

    @Value("${service.auth.token}")
    private String serviceAuthToken;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {
        System.out.println("Intercepted request to " + request.getRequestURI());

        String token = request.getHeader("X-Service-Auth");
        System.out.println("Received token: " + token);

        if (token == null || !token.equals(serviceAuthToken)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Unauthorized - invalid service auth token");
            return;
        }

        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        return !path.startsWith("/courses/internal/");
    }
}