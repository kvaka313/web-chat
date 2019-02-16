package com.infopulse.services.ControllerServices;

import com.infopulse.dto.ChatUserDto;
import com.infopulse.exceptions.IncorrectParameterException;
import com.infopulse.services.dataservices.BanDataService;
import org.springframework.stereotype.Service;

@Service
public class BanControllerService {

    private BanDataService banDataService;

    public BanControllerService(BanDataService banDataService){
        this.banDataService = banDataService;
    }

    public void addUserToBan(ChatUserDto chatUserDto) {
        String login = chatUserDto.getLogin();
        if (login == null) {
            throw new IncorrectParameterException("Login is required");
        }

        banDataService.addUserToBan(login);
    }

    public void removeUserFromBan(String login){
        if (login == null) {
            throw new IncorrectParameterException("Login is required");
        }

        banDataService.removeFromBan(login);
    }

    public boolean verifyForBan(String login){
        return banDataService.verifyForBan(login);
    }
}
