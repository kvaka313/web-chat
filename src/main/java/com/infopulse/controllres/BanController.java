package com.infopulse.controllres;

import com.infopulse.dto.ChatUserDto;
import com.infopulse.services.ControllerServices.BanControllerService;
import org.springframework.web.bind.annotation.*;

@RestController
public class BanController {

    private BanControllerService banControllerService;

    public BanController(BanControllerService banControllerService){
        this.banControllerService = banControllerService;
    }

    @CrossOrigin
    @RequestMapping(value="/ban", method = RequestMethod.POST)
    public void addUserToBan(@RequestBody ChatUserDto chatUserDto){
        banControllerService.addUserToBan(chatUserDto);
    }

    @CrossOrigin
    @RequestMapping(value="/ban/{login}", method = RequestMethod.DELETE)
    public void removeUserFromBan(@PathVariable("login") String login){
        banControllerService.removeUserFromBan(login);
    }
}
