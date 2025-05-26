package com.nastinka_krd.user_banking_service.service;

import io.jsonwebtoken.Claims;

import java.security.Key;
import java.util.Date;
import java.util.function.Function;

public interface JwtService {
    String extractUsername(String token);
    <T> T extractClaim(String token, Function<Claims, T> claimsResolver);
    Claims extractAllClaims(String token);
    Key getSignInKey();
    String generateAccessToken(String email);
    Date extractExpiration(String token);
    Boolean isTokenExpired(String token);
}
