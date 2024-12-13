package com.contenttree.Websocket;

import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

@Service
public class NotificationService
{
    private final NotificationWebSocketHandler webSocketHandler;


    public NotificationService(NotificationWebSocketHandler webSocketHandler) {
        this.webSocketHandler = webSocketHandler;
    }

    public void sendNotificationToAll(String message) {
        try {
            webSocketHandler.sendNotification(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void sendNotificationToUser(String sessionId, String message) {
        try {
            for (WebSocketSession session : webSocketHandler.getSessions()) {
                if (session.getId().equals(sessionId) && session.isOpen()) {
                    session.sendMessage(new TextMessage(message));
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
