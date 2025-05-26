package com.nastinka_krd.user_banking_service.controller;

import com.nastinka_krd.user_banking_service.service.GoogleAuthService;
import com.nastinka_krd.user_banking_service.service.JwtService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@Log4j2
public class OAuthController {
    private final GoogleAuthService googleAuthService;
    private final JwtService jwtService;

    @GetMapping("/oauth2/callback")
    public void handleCallback(@RequestParam("code") String code, HttpServletResponse response) {
        String accessToken = googleAuthService.exchangeCodeForAccessToken(code);
        Map<String, Object> userInfo = googleAuthService.getUserInfo(accessToken);
        String email = userInfo.get("email").toString();
        log.info("Authenticated user: {}", email);
        log.info("Access token: {}", jwtService.generateAccessToken(email));
        response.addHeader("Authorization", "Bearer " + accessToken);
    }
}
