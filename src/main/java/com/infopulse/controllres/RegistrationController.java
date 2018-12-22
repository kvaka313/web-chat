package com.infopulse.controllres;

import com.infopulse.converters.ChatUserConvert;
import com.infopulse.dto.ChatUserDto;
import com.infopulse.services.RegistrationService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class RegistrationController {

    private RegistrationService registrationService;

    public RegistrationController(RegistrationService registrationService){
        this.registrationService = registrationService;
    }

    @RequestMapping(value = "/registration", method = RequestMethod.POST)
    public void registration(@Valid @RequestBody  ChatUserDto chatUserDto){
         registrationService.saveUser(chatUserDto);
    }

}
