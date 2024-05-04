package org.example.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.constants.SecurityConstants;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.management.BadAttributeValueExpException;
import java.io.IOException;

public class JWTTokenValidatorFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String jwt = request.getHeader(SecurityConstants.JWT_HEADER);
        if (null != jwt) {
            try {

            } catch (Exception e) {
                throw new BadAttributeValueExpException("Invalid JWT Token");
            }
        }
        filterChain.doFilter(request, response);
    }
}