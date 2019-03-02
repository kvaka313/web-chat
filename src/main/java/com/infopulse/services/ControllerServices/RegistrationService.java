package com.infopulse.services.ControllerServices;

import com.infopulse.converters.ChatUserConvert;
import com.infopulse.services.dataservices.ChatUserDataService;
import com.infopulse.dto.ChatUserDto;
import com.infopulse.entities.ChatUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class RegistrationService {

    private ChatUserDataService chatUserDataService;

    private ApplicationEventPublisher applicationEventPublisher;

    private ChatUserConvert chatUserConvert;

    public RegistrationService(ChatUserDataService chatUserDataService,
                               ChatUserConvert chatUserConvert,
                               ApplicationEventPublisher applicationEventPublisher){
        this.chatUserDataService = chatUserDataService;
        this.chatUserConvert =chatUserConvert;
        this.applicationEventPublisher = applicationEventPublisher;
    }

    public void saveUser(ChatUserDto chatUserDto){
        ChatUser chatUser = chatUserDataService.saveUser(chatUserConvert.convertToEntity(chatUserDto));
        ChatUserDto newChatUserDto = chatUserConvert.convertToDto(chatUser);
        newChatUserDto.setPassword(chatUserDto.getPassword());
        applicationEventPublisher.publishEvent(newChatUserDto);
        log.trace(String.format("Event for user with login %s was generated", newChatUserDto.getLogin()));

    }

    public List<ChatUserDto> getAllUsers(){
        return chatUserDataService.getAll().stream()
                .map(entity -> chatUserConvert.convertToDto(entity))
                .collect(Collectors.toList());
    }
}
