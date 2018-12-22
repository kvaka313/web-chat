package com.infopulse.converters;

import com.infopulse.dto.ChatUserDto;
import com.infopulse.entities.ChatUser;
import org.springframework.stereotype.Component;

@Component
public class ChatUserConvert {

    public ChatUser convertToEntity(ChatUserDto chatUserDto){
        ChatUser chatUser =new ChatUser();
        chatUser.setLogin(chatUserDto.getLogin());
        chatUser.setName(chatUserDto.getName());
        chatUser.setPassword(chatUserDto.getPassword());
        return chatUser;
    }
}
