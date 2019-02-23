package com.infopulse.services.dataservices;

import com.infopulse.entities.ChatUser;
import com.infopulse.exceptions.UserAlreadyExist;
import com.infopulse.repositories.ChatUserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ChatUserDataServiceTest {

    @InjectMocks
    private ChatUserDataService chatUserDataService;

    @Mock
    private ChatUserRepository chatUserRepository;


    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }


    @Test
    public void saveUserTest_UserNotExists(){

        ChatUser chatUser = new ChatUser();
        chatUser.setName("name");
        chatUser.setLogin("login");
        when(chatUserRepository.findByLogin(chatUser.getLogin())).thenReturn(null);

        chatUserDataService.saveUser(chatUser);

        verify(chatUserRepository, times(1)).save(chatUser);
    }

    @Test(expected = UserAlreadyExist.class)
    public void saveUserTest_UserExists(){

        ChatUser chatUser = new ChatUser();
        chatUser.setName("name");
        chatUser.setLogin("login");
        when(chatUserRepository.findByLogin(chatUser.getLogin())).thenReturn(chatUser);

        chatUserDataService.saveUser(chatUser);

    }
}
