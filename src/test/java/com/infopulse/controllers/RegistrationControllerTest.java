package com.infopulse.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.infopulse.ApplicationWebChat;
import com.infopulse.dto.ChatUserDto;
import com.infopulse.utils.TokenUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.keycloak.KeycloakSecurityContext;
import org.keycloak.representations.AccessToken;
import org.modelmapper.internal.bytebuddy.matcher.ElementMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ApplicationWebChat.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql("/test-data.sql")
public class RegistrationControllerTest {


    @Resource
    private FilterChainProxy springSecurityFilterChain;

    @Autowired
    private WebApplicationContext webApplicationContext;

    private static final String testTenant = "chat";

    public static final KeycloakSecurityContext SECURITY_CONTEXT_WILCO =
            new KeycloakSecurityContext(StringUtils.EMPTY, createAccessToken(testTenant) ,StringUtils.EMPTY, null);

    private static AccessToken createAccessToken(String tenantId){
        AccessToken accessToken =new AccessToken();
        accessToken.issuer(tenantId);
        accessToken.expiration(1690326346);
        Map<String, AccessToken.Access> resourceAccess = new HashMap<>();
        AccessToken.Access access = new AccessToken.Access();
        access.addRole("ROLE_ADMIN");
        resourceAccess.put("chat-service", access);
        accessToken.setResourceAccess(resourceAccess);
        return accessToken;
    }

    @Test
    public void registrationTest_UserCreated() throws Exception {

        ChatUserDto chatUserDto = new ChatUserDto();
        chatUserDto.setName("testuser");
        chatUserDto.setLogin("chatuserLogin");
        chatUserDto.setPassword("password");

        String url = "/registration";

        ObjectMapper mapper = new ObjectMapper();

        MockHttpServletRequestBuilder msb = post(url)
                .content(mapper.writeValueAsString(chatUserDto))
                .contentType(MediaType.APPLICATION_JSON);

        MockMvc mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .defaultRequest(get("/"))
                .build();

        ResultActions resultActions = mvc.perform(msb);

        resultActions.andExpect(status().isOk());
    }

    @Test
    public void registrationTest_UserAlreadyExists() throws Exception {

        ChatUserDto chatUserDto = new ChatUserDto();
        chatUserDto.setName("qqq");
        chatUserDto.setLogin("qqq");
        chatUserDto.setPassword("password");

        String url = "/registration";

        ObjectMapper mapper = new ObjectMapper();

        MockHttpServletRequestBuilder msb = post(url)
                .content(mapper.writeValueAsString(chatUserDto))
                .contentType(MediaType.APPLICATION_JSON);

        MockMvc mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .defaultRequest(get("/"))
                .build();

        ResultActions resultActions = mvc.perform(msb);

        resultActions
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", is("User with login qqq already exist")));

    }

    @Test
    public void getAllUsersTest() throws Exception {
        ChatUserDto chatUserDto = new ChatUserDto();
        chatUserDto.setName("testuser11");
        chatUserDto.setLogin("chatuserLogin11");
        chatUserDto.setPassword("password11");

        String url = "/registration";

        ObjectMapper mapper = new ObjectMapper();

        MockHttpServletRequestBuilder msb = post(url)
                .content(mapper.writeValueAsString(chatUserDto))
                .contentType(MediaType.APPLICATION_JSON);

        MockMvc mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .defaultRequest(get("/"))
                .build();

        ResultActions resultActions = mvc.perform(msb);

        resultActions.andExpect(status().isOk());

        String urlGet = "/users";

        mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .addFilters(springSecurityFilterChain)
                .defaultRequest(get("/"))
                .build();

        resultActions = mvc.perform(get(urlGet)
                .header("Authorization", "bearer "+ TokenUtils.getRawToken(testTenant, "myadmin","qwertyui")));

        resultActions.andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.[0].name", is("qqq")))
                .andExpect(jsonPath("$.[0].login", is("qqq")))
                .andExpect(jsonPath("$.[1].name", is("testuser11")))
                .andExpect(jsonPath("$.[1].login", is("chatuserLogin11")))
                .andExpect(jsonPath("$", hasSize(2)));


    }




}
