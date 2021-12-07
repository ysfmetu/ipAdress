package com.ysf.ipadress.security;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ysf.ipadress.repo.KullaniciRepostories;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

@Component
@RequiredArgsConstructor
public class AuthenticationFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;
    private KullaniciRepostories repository;
    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        String token = parseToken(httpServletRequest);
        LoggedUserModel userDetails = null;
        if (token != null) {
            try {
                if (jwtUtil.validateToken(token)) {
                    userDetails = jwtUtil.getAuthenticationFromToken(token);
                }
            } catch (JsonProcessingException e) {
                throw new IOException("user not found");
            }

            UsernamePasswordAuthenticationToken authToken=new UsernamePasswordAuthenticationToken(userDetails, Collections.EMPTY_LIST);
            authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));
            SecurityContextHolder.getContext().setAuthentication(authToken);
        }
        filterChain.doFilter(httpServletRequest,httpServletResponse);
    }

    private String parseToken(HttpServletRequest httpServletRequest) {
        String token = httpServletRequest.getHeader("Authentication");
        if (StringUtils.hasText(token) && token.startsWith("Bearer ")) {
            return token.substring(7);
        }
        return null;
    }
}
