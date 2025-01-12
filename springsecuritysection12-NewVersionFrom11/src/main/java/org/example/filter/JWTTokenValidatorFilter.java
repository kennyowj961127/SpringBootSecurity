package org.example.filter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.constants.ApplicationConstants;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class JWTTokenValidatorFilter extends OncePerRequestFilter {

        @Override
        protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
            String jwt = request.getHeader("Authorization");
            if(jwt != null) {
                try {
                    Environment environment = getEnvironment();
                    if(environment != null) {
                        String secret = environment.getProperty(ApplicationConstants.JWT_SECRET, ApplicationConstants.JWT_SECRET_DEFAULT);
                        SecretKey secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
                        if(secretKey != null) {
                            Claims claims = Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(jwt).getPayload();
                            if(claims != null) {
                                String username = claims.get("username", String.class);
                                String authorities = claims.get("authorities", String.class);
                                request.setAttribute("username", username);
                                request.setAttribute("authorities", authorities);
                                Authentication authentication = new UsernamePasswordAuthenticationToken(username, null,
                                        AuthorityUtils.commaSeparatedStringToAuthorityList(authorities));
                                SecurityContextHolder.getContext().setAuthentication(authentication);
                            }
                        }
                    }
                } catch (ExpiredJwtException e){
                    filterChain.doFilter(request, response);
                    return;
                }
                catch (Exception e) {
                    throw new BadCredentialsException("Invalid JWT token: " + e.getMessage());
                }
            }
            filterChain.doFilter(request, response);
        }

        @Override
        protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
            return request.getServletPath().equals("/user");
        }
}
