package com.contenttree.Websocket;

import org.springframework.web.socket.WebSocketSession;

import java.util.concurrent.ConcurrentHashMap;

public class UserSessionManager {
    private final ConcurrentHashMap<Long, WebSocketSession> userSessions = new ConcurrentHashMap<>();

    public void addSession(Long userId, WebSocketSession session) {
        userSessions.put(userId, session);
    }

    public void removeSession(Long userId) {
        userSessions.remove(userId);
    }

    public WebSocketSession getSession(Long userId) {
        return userSessions.get(userId);
    }
}
