package com.ysf.ipadress.security;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class JwtUtilImpl implements com.ysf.ipadress.security.JwtUtil {
    private final HttpServletRequest request;
    private final ObjectMapper mapper;

    @Value("${jwt.secret}")
    private String jwtSecret;
    @Value("${jwt.expiration}")
    private int jwtExpiration;
    @Override
    public String generateToken(Authentication authentication) throws JsonProcessingException {
        com.ysf.ipadress.security.LoggedUserModel user = (com.ysf.ipadress.security.LoggedUserModel) authentication.getPrincipal();
        return generateToken(user);
    }

    public LoggedUserModel getAuthenticationFromToken(String token) throws JsonProcessingException {
        String readToken = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token)
                .getBody().getSubject();
        return mapper.readValue(readToken, com.ysf.ipadress.security.LoggedUserModel.class);
    }

    @Override
    public boolean validateToken(String token) throws JsonProcessingException {
        Jws<Claims> claimsJws = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token);
        mapper.readValue(claimsJws.getBody().getSubject(),com.ysf.ipadress.security.LoggedUserModel.class);
        return claimsJws.getBody().get("ip").equals(request.getRemoteAddr());
    }

    @Override
    public String refreshToken() throws JsonProcessingException {
        return generateToken(request);
    }

    @Override
    public String generateToken(com.ysf.ipadress.security.LoggedUserModel logged) throws JsonProcessingException {
        Map<String, Object> extraInfo = new HashMap<>();
        extraInfo.put("ip", request.getRemoteAddr());

        Date date = new Date();
        return Jwts.builder()
                .setSubject(mapper.writeValueAsString(logged))
                .addClaims(extraInfo)
                .setIssuedAt(date)
                .setExpiration(new Date(date.getTime() + jwtExpiration))
                .setIssuer("Tmo Hasat Piyasa")
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }

    private String generateToken(HttpServletRequest request) throws JsonProcessingException {
        String currentToken = request.getHeader("Authorization").substring(7);
        if (validateToken(currentToken)) {
            com.ysf.ipadress.security.LoggedUserModel logged = getAuthenticationFromToken(currentToken);
            return generateToken(logged);
        }
        return null;
    }
}
