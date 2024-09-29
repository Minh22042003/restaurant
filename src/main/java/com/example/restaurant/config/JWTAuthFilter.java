package com.example.restaurant.config;

import com.example.restaurant.service.JWTUtils;
import com.example.restaurant.service.UserDetailService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JWTAuthFilter extends OncePerRequestFilter {
    @Autowired
    private JWTUtils jwtUtils;

    @Autowired
    private UserDetailService userDetailService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final String authHead = request.getHeader("Authorization");
        final String jwtToken;
        final String userEmail;

        if (authHead == null || authHead.isBlank()){
            filterChain.doFilter(request,response);
            return;
        }

        jwtToken = authHead.substring(7);
        userEmail = jwtUtils.exactUserName(jwtToken);
        System.out.println("Processing token: " + jwtToken);
        System.out.println("Extracted username: " + userEmail);

        if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null){
            UserDetails userDetails = userDetailService.loadUserByUsername(userEmail);

            if (jwtUtils.isTokenValid(jwtToken, userDetails)){
                SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
                UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                        userDetails, null,userDetails.getAuthorities()
                );

                token.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                securityContext.setAuthentication(token);
                SecurityContextHolder.setContext(securityContext);
            }

        }
        filterChain.doFilter(request, response);
    }
}
