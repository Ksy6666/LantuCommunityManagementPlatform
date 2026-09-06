package com.example.lantu_web_java.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

public class JwtUtil {

    // 开发环境下使用固定密钥（生产环境应通过配置注入）
    private static final String SECRET = "OpenLantuSecretKeyForJWT2024MustBe256BitsLong!!";
    private static final long EXPIRATION = 86400000L; // 24 小时

    private static final SecretKey KEY = Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8));

    /**
     * 生成 JWT token
     * @param userId 用户 ID
     * @param account 用户账号
     * @return JWT token 字符串
     */
    public static String generateToken(Long userId, String account) {
        Date now = new Date();
        return Jwts.builder()
                .subject(userId.toString())
                .claim("account", account)
                .issuedAt(now)
                .expiration(new Date(now.getTime() + EXPIRATION))
                .signWith(KEY)
                .compact();
    }

    /**
     * 解析 token 中的用户 ID
     * @param token JWT token
     * @return 用户 ID
     */
    public static Long getUserIdFromToken(String token) {
        Claims claims = parseToken(token);
        return Long.parseLong(claims.getSubject());
    }

    /**
     * 验证 token 是否有效
     * @param token JWT token
     * @return true 有效，false 无效或过期
     */
    public static boolean validateToken(String token) {
        try {
            parseToken(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private static Claims parseToken(String token) {
        return Jwts.parser()
                .verifyWith(KEY)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
