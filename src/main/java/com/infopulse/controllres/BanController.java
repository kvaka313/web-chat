package com.infopulse.controllres;

import com.infopulse.dto.ChatUserDto;
import com.infopulse.services.ControllerServices.BanControllerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
public class BanController {

    private BanControllerService banControllerService;

    public BanController(BanControllerService banControllerService){
        this.banControllerService = banControllerService;
    }

    @CrossOrigin
    @RequestMapping(value="/ban", method = RequestMethod.POST)
    public void addUserToBan(@RequestBody ChatUserDto chatUserDto){
        log.debug(String.format("Call ban endpoint for login %s", chatUserDto.getLogin()));
        banControllerService.addUserToBan(chatUserDto);
    }

    @CrossOrigin
    @RequestMapping(value="/ban/{login}", method = RequestMethod.DELETE)
    public void removeUserFromBan(@PathVariable("login") String login){
        banControllerService.removeUserFromBan(login);
    }
}
