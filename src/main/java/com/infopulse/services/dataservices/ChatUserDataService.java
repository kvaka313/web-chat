package com.infopulse.services.dataservices;

import com.infopulse.entities.ChatUser;
import com.infopulse.exceptions.UserAlreadyExist;
import com.infopulse.repositories.ChatUserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
public class ChatUserDataService {

    private ChatUserRepository chatUserRepository;

    public ChatUserDataService(ChatUserRepository chatUserRepository){
        this.chatUserRepository = chatUserRepository;
    }

    @Transactional
    public ChatUser saveUser(ChatUser chatUser){
        ChatUser newChatUser = chatUserRepository.findByLogin(chatUser.getLogin());
        if(newChatUser != null){
            log.warn(String.format("User with login %s already exist", chatUser.getLogin()));
            throw new UserAlreadyExist(String.format("User with login %s already exist",chatUser.getLogin()));
        }
        ChatUser chatUserSave = chatUserRepository.save(chatUser);
        log.info(String.format("User %s has been saved",chatUserSave.getLogin()));
        return chatUserSave;
    }

    public List<ChatUser> getAll(){
        return chatUserRepository.findAll();
    }
}
