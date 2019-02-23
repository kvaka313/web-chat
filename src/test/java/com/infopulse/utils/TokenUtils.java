package com.infopulse.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

import static java.lang.String.format;

public class TokenUtils {

    public static String getRawToken(String tenantId, String username, String password) throws IOException, URISyntaxException {

        URI url = new URI(format("http://127.0.0.1:%s/auth/realms/%s/protocol/openid-connect/token", "8080", tenantId));

        // populate request body
        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("grant_type", "password");
        requestBody.add("client_id", "ADMIN-UI");
        requestBody.add("username", username);
        requestBody.add("password", password);
        requestBody.add("scope", "offline_access");

        // create request
        RequestEntity<MultiValueMap<String, String>> request = RequestEntity.post(url)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(requestBody);

        // perform request to Embedded IDM
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(request, String.class);

        // parse response and return only access token
        String jsonBody = response.getBody();
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> tokenMap = mapper.readValue(jsonBody, new TypeReference<Map<String, String>>() {
        });

        return (String) tokenMap.get("access_token");
    }
}
