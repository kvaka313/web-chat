package com.infopulse.services.ControllerServices;

import com.infopulse.converters.MessageConverter;
import com.infopulse.dto.SendMessage;
import com.infopulse.entities.Message;
import com.infopulse.entities.RedisMessage;
import com.infopulse.services.dataservices.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WebSocketServiceController {
    @Autowired
    private MessageService messageService;

    @Autowired
    private MessageConverter messageConverter;

    public List<SendMessage> getAllMessage(String receiver){
        List<Message> messages = messageService.getAllPrivateMessages(receiver);
        List<SendMessage> privateMessages = messageConverter.convertListPrivateMessagesToSendMessages(messages);
        List<RedisMessage> broadcastMessages = messageService.getAllBroadCastMessages();
        List<SendMessage> broadMessages = messageConverter.convertListBroadcastMessagesToSendMessages(broadcastMessages);
        privateMessages.addAll(broadMessages);
        return privateMessages;
    }

   //delete from postgres
    public void deleteAllPrivateMessages(String receiver){
        messageService.deleteAllPrivateMessages(receiver);
    }

   //redis
    public void saveBroadcastMessage(SendMessage sendMessage){
        messageService.saveBroadcastMessage(sendMessage.getSender(), sendMessage.getMessage());
    }

   //postgresql
    public void savePrivateMessage(String to, SendMessage sendMessage){
        messageService.savePrivateMessage(to, sendMessage.getSender(), sendMessage.getMessage());
    }

}
