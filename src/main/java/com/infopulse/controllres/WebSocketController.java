package com.infopulse.controllres;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.infopulse.dto.ReceiveMessage;
import com.infopulse.dto.SendMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.io.IOException;
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
        if(!violations.isEmpty()){
            //send error message
            String errorMessage = getErrorMessage(violations);
            SendMessage sendMessage = createSendMessage(session.getPrincipal().getName(), errorMessage, "ERROR");
            sendPrivateMessage(session.getPrincipal().getName(), sendMessage);
            return;
        }
        switch(receiveMessage.getType()){
            case "CONNECT":{
                activeUsers.put(session.getPrincipal().getName(), session);
                webSocketService.getAllMessage(session.getPrincipal().getName())
                        .stream()
                        .peek(m -> sendPrivateMessage(session.getPrincipal().getName(), m)).count();
                webSocketService.deleteAllPrivateMessages(session.getPrincipal().getName());
                sendAllChangeActiveList();
            }
            case "BROADCAST":{
                SendMessage sendMessage = createSendMessage(session.getPrincipal().getName(),
                        receiveMessage.getMessage(),
                        "BROADCAST");
                this.sendAll(sendMessage);
                webSocketService.saveBroadcastMessage(sendMessage);

            }
            case "PRIVATE":{
                SendMessage sendMessage = createSendMessage(session.getPrincipal().getName(),
                        receiveMessage.getMessage(),
                        "PRIVATE");
                if(isActiveUser(receiveMessage.getReceiver())){
                    sendPrivateMessage(receiveMessage.getReceiver(), sendMessage);
                }else{
                    webSocketService.savePrivateMessage(receiveMessage.getReceiver(), sendMessage);
                }
            }
            case "LOGOUT":{
                SendMessage sendMessage = createSendMessage(session.getPrincipal().getName(),
                        "",
                        "LOGOUT");
                sendPrivateMessage(session.getPrincipal().getName(), sendMessage);
                removeFromActiveUsers(session.getPrincipal().getName());
            }
        }



    }

}
