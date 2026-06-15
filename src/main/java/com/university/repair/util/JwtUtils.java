package com.university.repair.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * JWT 工具类
 */
@Slf4j
@Component
public class JwtUtils {

    @Value("${jwt.secret:dormitory-repair-system-secret-key-2024}")
    private static String SECRET;

    @Value("${jwt.expiration:3600000}")
    private static Long EXPIRATION;

    static {
        // 静态初始化
        SECRET = "dormitory-repair-system-secret-key-2024";
        EXPIRATION = 3600000L;
    }

    /**
     * 生成JWT Token
     */
    public static String generateToken(Integer userId, String userRole) {
        try {
            Date expiresAt = new Date(System.currentTimeMillis() + EXPIRATION);
            return JWT.create()
                    .withClaim("userId", userId)
                    .withClaim("userRole", userRole)
                    .withExpiresAt(expiresAt)
                    .sign(Algorithm.HMAC256(SECRET));
        } catch (Exception e) {
            log.error("生成Token失败", e);
            throw new RuntimeException("Token生成失败");
        }
    }

    /**
     * 验证JWT Token
     */
    public static boolean verify(String token) {
        try {
            JWTVerifier verifier = JWT.require(Algorithm.HMAC256(SECRET)).build();
            verifier.verify(token);
            return true;
        } catch (JWTVerificationException e) {
            log.error("Token验证失败", e);
            return false;
        }
    }

    /**
     * 获取Token中的userId
     */
    public static Integer getUserId(String token) {
        try {
            DecodedJWT decodedJWT = JWT.decode(token);
            return decodedJWT.getClaim("userId").asInt();
        } catch (Exception e) {
            log.error("获取userId失败", e);
            return null;
        }
    }

    /**
     * 获取Token中的userRole
     */
    public static String getUserRole(String token) {
        try {
            DecodedJWT decodedJWT = JWT.decode(token);
            return decodedJWT.getClaim("userRole").asString();
        } catch (Exception e) {
            log.error("获取userRole失败", e);
            return null;
        }
    }
}
