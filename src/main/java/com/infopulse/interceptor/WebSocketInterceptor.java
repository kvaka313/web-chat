package com.infopulse.interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.codec.binary.Base64;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class WebSocketInterceptor implements HandshakeInterceptor {
    @Override
    public boolean beforeHandshake(ServerHttpRequest request,
                                   ServerHttpResponse response,
                                   WebSocketHandler wsHandler,
                                   Map<String, Object> attributes) throws Exception {
        ServletServerHttpRequest servletRequest = (ServletServerHttpRequest) request;
        String token = servletRequest
                            .getServletRequest()
                            .getParameter("Authorization");
        Date expr = getExpireDate(token);
        Date current = new Date();
        if(expr.before(current)){
            response.setStatusCode(HttpStatus.FORBIDDEN);
            return false;
        }

        String role = getRole(token);
        if(!"ROLE_USER".equals(role)){
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            return false;
        }
        String login = getUserLogin(token);
        if(login == null){
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            return false;
        }
        attributes.put("login", login);

        return true;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception exception) {

    }

    private String getRole(String token) throws IOException {
        String[] tokenParts = token.split("\\.");
        String body = tokenParts[1];
        try {
            String jsonString = new String(Base64.decodeBase64(body), "UTF-8");
            ObjectMapper mapper =new ObjectMapper();
            Map<String, Object> json = mapper.readValue(jsonString, Map.class);
            Map<String, Object> resource = (Map<String, Object>)json.get("resource_access");
            Map<String, Object> service = (Map<String, Object>)resource.get("chat-service");
            List<String> roles = (List<String>)service.get("roles");
            return roles.get(0);
        } catch (UnsupportedEncodingException e) {
           throw new RuntimeException(e);
        }
    }

    private Date getExpireDate(String token) throws IOException {
        String[] tokenParts = token.split("\\.");
        String body = tokenParts[1];
        try {
            String jsonString = new String(Base64.decodeBase64(body), "UTF-8");
            ObjectMapper mapper =new ObjectMapper();
            Map<String, Object> json = mapper.readValue(jsonString, Map.class);
            Integer exp = (Integer)json.get("exp");
            String expDate = exp.toString()+"000";
            return new Date(Long.parseLong(expDate));
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    private String getUserLogin(String token) throws IOException {
        String[] tokenParts = token.split("\\.");
        String body = tokenParts[1];
        try {
            String jsonString = new String(Base64.decodeBase64(body), "UTF-8");
            ObjectMapper mapper =new ObjectMapper();
            Map<String, Object> json = mapper.readValue(jsonString, Map.class);
            String login = (String)json.get("preferred_username");
            return login;
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }
}
