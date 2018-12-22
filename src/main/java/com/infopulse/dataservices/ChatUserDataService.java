package com.infopulse.dataservices;

import com.infopulse.entities.ChatUser;
import com.infopulse.exceptions.UserAlreadyExist;
import com.infopulse.repositories.ChatUserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
            throw new UserAlreadyExist(String.format("User with login %s already exist",chatUser.getLogin()));
        }

        return chatUserRepository.save(chatUser);
    }
}
