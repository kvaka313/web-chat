package com.infopulse.controllres;

import com.infopulse.converters.ChatUserConvert;
import com.infopulse.dto.ChatUserDto;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class RegistrationController {

    private RegistrationService registrationService;

    private ChatUserConvert chatUserConvert;

    public RegistrationController(RegistrationService registrationService,
                                  ChatUserConvert chatUserConvert){
        this.registrationService = registrationService;
        this.chatUserConvert = chatUserConvert;
    }

    @RequestMapping(params = "/registration", method = RequestMethod.POST)
    public void registration(@Valid ChatUserDto chatUserDto){
         registrationService.saveUser(chatUserConvert.convertToEntity(chatUserDto));
    }

}
