package com.nastinka_krd.user_banking_service.service.impl;

import com.nastinka_krd.user_banking_service.service.GoogleAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

import com.warrenstrange.googleauth.GoogleAuthenticator;

@Service
@RequiredArgsConstructor
public class GoogleAuthServiceImpl implements GoogleAuthService {

    private final RestTemplate restTemplate;

    private final GoogleAuthenticator gAuth;

    @Value("${myapp.clientId}")
    private String clientId;

    @Value("${myapp.clientSecret}")
    private String clientSecret;

    @Value("${myapp.redirectedUrl}")
    private String redirectedUrl;



    @Override
    public String exchangeCodeForAccessToken(String code) {
        String tokenUrl = "https://oauth2.googleapis.com/token";

        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("code", code);
        requestBody.put("client_id", clientId);
        requestBody.put("client_secret", clientSecret);
        requestBody.put("redirect_uri", redirectedUrl);
        requestBody.put("grant_type", "authorization_code");

        ResponseEntity<Map> response = restTemplate.postForEntity(tokenUrl, requestBody, Map.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            return (String) response.getBody().get("access_token");
        } else {
            throw new RuntimeException("Failed to exchange code for access token");
        }
    }

    @Override
    public Map<String, Object> getUserInfo(String accessToken) {
        String userInfoUrl = "https://www.googleapis.com/oauth2/v2/userinfo?access_token=" + accessToken;

        ResponseEntity<Map> response = restTemplate.getForEntity(userInfoUrl, Map.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            return response.getBody();
        } else {
            throw new RuntimeException("Failed to get user info");
        }
    }

}