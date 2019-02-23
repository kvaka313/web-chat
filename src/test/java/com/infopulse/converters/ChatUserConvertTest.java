package com.infopulse.converters;

import com.infopulse.dto.ChatUserDto;
import com.infopulse.entities.ChatUser;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ChatUserConvertTest {
    private ChatUserConvert chatUserConvert = new ChatUserConvert();

    @Test
    public void convertToEntityTest(){
        ChatUserDto chatUserDto = new ChatUserDto();
        chatUserDto.setName("name");
        chatUserDto.setLogin("login");

        ChatUser chatUser = chatUserConvert.convertToEntity(chatUserDto);

        assertEquals("name",chatUser.getName());
        assertEquals("login",chatUser.getLogin());

    }

}
