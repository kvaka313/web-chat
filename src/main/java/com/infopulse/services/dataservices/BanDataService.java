package com.infopulse.services.dataservices;

import com.infopulse.entities.Ban;
import com.infopulse.entities.ChatUser;
import com.infopulse.exceptions.UserAlreadyBanedException;
import com.infopulse.exceptions.UserCanNotBeUnBanedException;
import com.infopulse.exceptions.UserNotFoundException;
import com.infopulse.repositories.BanRepository;
import com.infopulse.repositories.ChatUserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BanDataService {

    private BanRepository banRepository;

    private ChatUserRepository chatUserRepository;

    public BanDataService(BanRepository banRepository, ChatUserRepository chatUserRepository){
        this.banRepository = banRepository;
        this.chatUserRepository = chatUserRepository;
    }

    @Transactional
    public void addUserToBan(String login){
        ChatUser chatUser = chatUserRepository.findByLogin(login);
        if(chatUser ==null){
            throw new UserNotFoundException(String.format("User with login %s does not exist", login));

        }

        if(chatUser.getBan()!=null){
            throw new UserAlreadyBanedException("User already was banned");
        }

        Ban ban =new Ban();
        ban.setChatuser(chatUser);
        banRepository.save(ban);

    }

    @Transactional
    public void removeFromBan(String login){
        ChatUser chatUser = chatUserRepository.findByLogin(login);
        if(chatUser == null){
            throw new UserNotFoundException(String.format("User with login %s does not exist", login));

        }

        if(chatUser.getBan() == null){
            throw new UserCanNotBeUnBanedException("User can not be deleted from ban");
        }
        Ban ban = chatUser.getBan();
        banRepository.delete(ban);
    }
}
