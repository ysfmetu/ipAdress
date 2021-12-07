package com.ysf.ipadress.security;

import lombok.RequiredArgsConstructor;
import net.minidev.json.JSONObject;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@RequiredArgsConstructor
public class AccessDeniedFilter implements AccessDeniedHandler {
    private final MessageSourceAccessor message;
    @Override
    public void handle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AccessDeniedException e) throws IOException, ServletException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(String.valueOf(EResult.error), false);
        jsonObject.put(EResult.error.name(), message.getMessage("auth.access.fail"));
        jsonObject.put(EResult.error.name(), 403);
        httpServletResponse.setContentType("application/json;charset=UTF-8");
        httpServletResponse.getWriter().write(jsonObject.toString());
    }
}
