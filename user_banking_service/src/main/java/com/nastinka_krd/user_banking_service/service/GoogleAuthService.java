package com.nastinka_krd.user_banking_service.service;

import java.util.Map;

public interface GoogleAuthService {
    String exchangeCodeForAccessToken(String code);

    Map<String, Object> getUserInfo(String accessToken);
}
