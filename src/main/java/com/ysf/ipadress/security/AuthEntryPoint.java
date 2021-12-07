package com.ysf.ipadress.security;

import lombok.RequiredArgsConstructor;
import net.minidev.json.JSONObject;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@RequiredArgsConstructor
public class AuthEntryPoint implements AuthenticationEntryPoint {
    private final MessageSourceAccessor message;

    @Override
    public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
        JSONObject jsonObject=new JSONObject();
        int resultCode;
        String mess;
        if (e instanceof InsufficientAuthenticationException){
            mess = message.getMessage("login.required");
            resultCode=401;
        }else{
            mess = e.getMessage();
            resultCode = 402;
        }
        jsonObject.put("error", false);
        jsonObject.put("message",mess);
        jsonObject.put("status", resultCode);
        httpServletResponse.setContentType("application/json;charset=UTF-8");
        httpServletResponse.getWriter().write(jsonObject.toString());
    }
    }


