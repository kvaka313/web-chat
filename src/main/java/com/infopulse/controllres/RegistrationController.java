package com.infopulse.controllres;

import com.infopulse.dto.ChatUserDto;
import com.infopulse.services.ControllerServices.RegistrationService;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@CrossOrigin
public class RegistrationController {

    private RegistrationService registrationService;

    public RegistrationController(RegistrationService registrationService){
        this.registrationService = registrationService;
    }

    @RequestMapping(value = "/registration", method = RequestMethod.POST)
    public void registration(@Valid @RequestBody  ChatUserDto chatUserDto){
         registrationService.saveUser(chatUserDto);
    }

    @RequestMapping(value="/users", method=RequestMethod.GET)
    public List<ChatUserDto> getAllUsers(){
        return registrationService.getAllUsers();
    }

}
