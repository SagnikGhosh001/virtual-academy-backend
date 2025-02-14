package com.smsv2.smsv2.config;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;
import java.util.Map;

public class WebSocketAuthInterceptor implements HandshakeInterceptor {

    private final JwtUtil jwtUtil;

    public WebSocketAuthInterceptor(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) {
        HttpHeaders headers = request.getHeaders();
        String authHeader = headers.getFirst("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);

            try {
                String username = jwtUtil.extractUsername(token);
                if (jwtUtil.validateToken(token, username)) {
                    attributes.put("username", username);
                    return true; // ✅ Authentication Success
                }
            } catch (Exception e) {
                return false; // ❌ Invalid Token
            }
        }
        return false; // ❌ No Token Provided
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception exception) {
        // Not needed
    }
}
