package com.infopulse.controllres;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.infopulse.dto.ReceiveMessage;
import com.infopulse.dto.SendMessage;
import com.infopulse.services.ControllerServices.WebSocketServiceController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class WebSocketController extends TextWebSocketHandler {

    @Autowired
    private WebSocketServiceController webSocketService;

    @Autowired
    Validator validator;

    private Map<String, WebSocketSession> activeUsers = new ConcurrentHashMap<>();

    private ObjectMapper mapper = new ObjectMapper();

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message)
            throws IOException {
        String jsonString = message.getPayload();
        ReceiveMessage receiveMessage = mapper.readValue(jsonString, ReceiveMessage.class);
        Set<ConstraintViolation<ReceiveMessage>> violations =
                validator.validate(receiveMessage, ReceiveMessage.class);
        //verify if there are no any errors.
        if (!violations.isEmpty()) {
            //send error message
            String errorMessage = getErrorMessage(violations);
            SendMessage sendMessage = createSendMessage(session.getPrincipal().getName(), errorMessage, "ERROR");
            sendPrivateMessage(session.getPrincipal().getName(), sendMessage);
            return;
        }
        switch (receiveMessage.getType()) {
            case "CONNECT": {
                activeUsers.put(session.getPrincipal().getName(), session);
                webSocketService.getAllMessage(session.getPrincipal().getName())
                        .stream()
                        .peek(m -> sendPrivateMessage(session.getPrincipal().getName(), m)).count();
                webSocketService.deleteAllPrivateMessages(session.getPrincipal().getName());
                sendAllChangeActiveList();
                break;
            }
            case "BROADCAST": {
                SendMessage sendMessage = createSendMessage(session.getPrincipal().getName(),
                        receiveMessage.getMessage(),
                        "BROADCAST");
                this.sendAll(sendMessage);
                webSocketService.saveBroadcastMessage(sendMessage);
                break;

            }
            case "PRIVATE": {
                SendMessage sendMessage = createSendMessage(session.getPrincipal().getName(),
                        receiveMessage.getMessage(),
                        "PRIVATE");
                if (isActiveUser(receiveMessage.getReceiver())) {
                    sendPrivateMessage(receiveMessage.getReceiver(), sendMessage);
                } else {
                    webSocketService.savePrivateMessage(receiveMessage.getReceiver(), sendMessage);
                }
                break;
            }
            case "LOGOUT": {
                SendMessage sendMessage = createSendMessage(session.getPrincipal().getName(),
                        "",
                        "LOGOUT");
                sendPrivateMessage(session.getPrincipal().getName(), sendMessage);
                removeFromActiveUsers(session.getPrincipal().getName());
                sendAllChangeActiveList();
                break;
            }
        }

    }

    private String getErrorMessage(Set<ConstraintViolation<ReceiveMessage>> violations) {
        return violations.iterator().next().getMessage();
    }

    private SendMessage createSendMessage(String from, String message, String type) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setType(type);
        sendMessage.setMessage(message);
        sendMessage.setSender(from);
        sendMessage.setUsersActive(null);
        return sendMessage;
    }

    private void sendPrivateMessage(String to, SendMessage sendMessage) {
        try {
            WebSocketSession session = activeUsers.get(to);
            session.sendMessage(
                    new TextMessage(mapper.writeValueAsString(sendMessage)));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void sendAllChangeActiveList() {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setType("LIST");
        sendMessage.setUsersActive(new ArrayList<>(activeUsers.keySet()));
        this.sendAll(sendMessage);
    }

    private void sendAll(SendMessage sendMessage) {
        try {
            TextMessage textMessage = new TextMessage(mapper.writeValueAsString(sendMessage));
            for (Map.Entry<String, WebSocketSession> item : activeUsers.entrySet()) {
                item.getValue().sendMessage(textMessage);
            }
        } catch (IOException e) {
            new RuntimeException(e);

        }
    }

    private boolean isActiveUser(String user) {
        return activeUsers.keySet().contains(user);
    }

    private void removeFromActiveUsers(String user){
        activeUsers.remove(user);
    }
}
