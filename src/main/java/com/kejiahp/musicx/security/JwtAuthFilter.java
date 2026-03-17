package com.kejiahp.musicx.security;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.kejiahp.musicx.apps.session.SessionService;
import com.kejiahp.musicx.apps.user.CustomUserService;
import com.kejiahp.musicx.apps.user.UserModel;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    @Autowired
    private JwtAuthService jwtService;
    @Autowired
    private CustomUserService customUserService;
    @Autowired
    private SessionService sessionService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = authHeader.substring(7);
        Optional<String> userId = jwtService.extractId(token);

        if (userId.isEmpty()) {
            filterChain.doFilter(request, response);
            return;
        }

        if (userId != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            Optional<UserModel> user = customUserService.getUserById(userId.get());

            if (user.isEmpty()) {
                filterChain.doFilter(request, response);
                return;
            }

            if (jwtService.isTokenIdValid(token, user.get())) {
                Optional<String> sid = jwtService.extractSid(token);
                if (sid.isEmpty()) {
                    filterChain.doFilter(request, response);
                    return;
                }

                if (!sessionService.isSessionValid(user.get(), sid.get())) {
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    return;
                }

                // Added the authorities list with a generic role string "USER"
                // Because I wanted the `isAuthenticated` method of the
                // `SecurityContextHolder.getContext().getAuthentication()` to return true.
                UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(user.get(), null,
                        List.of(new SimpleGrantedAuthority("USER")));

                // This adds metadata like, IP address, Session ID, User agent
                auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // Store authentication in security context
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        }

        filterChain.doFilter(request, response);
    }
}
