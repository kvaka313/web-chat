package com.infopulse.converters;

import com.infopulse.dto.SendMessage;
import com.infopulse.entities.Message;
import com.infopulse.entities.RedisMessage;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class MessageConverter {
    public SendMessage convertToDto(Message message){
        SendMessage sendMessage =new SendMessage();
        sendMessage.setType("PRIVATE");
        sendMessage.setMessage(message.getBody());
        sendMessage.setSender(message.getSender().getLogin());
        return sendMessage;
    }

    public SendMessage convertToDtoFromRedisMessage(RedisMessage redisMessage){
        SendMessage sendMessage =new SendMessage();
        sendMessage.setType("BROADCAST");
        sendMessage.setSender(redisMessage.getSender());
        sendMessage.setMessage(redisMessage.getMessage());
        return sendMessage;
    }

    public List<SendMessage> convertListPrivateMessagesToSendMessages(List<Message> listMessage){
        return listMessage.stream()
                .map(entity -> convertToDto(entity))
                .collect(Collectors.toList());
    }

    public List<SendMessage> convertListBroadcastMessagesToSendMessages(List<RedisMessage> listMessage){
        return listMessage.stream()
                .map(entity -> convertToDtoFromRedisMessage(entity))
                .collect(Collectors.toList());
    }
}
