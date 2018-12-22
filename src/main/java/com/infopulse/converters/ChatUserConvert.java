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

    public ChatUserDto convertToDto(ChatUser chatUser){
        ChatUserDto chatUserDto = new ChatUserDto();
        chatUserDto.setName(chatUser.getName());
        chatUserDto.setPassword(chatUser.getPassword());
        chatUserDto.setLogin(chatUser.getLogin());
        return chatUserDto;
    }
}
