package org.pockettech.qiusheng.utils;

import io.jsonwebtoken.Claims;
import org.pockettech.qiusheng.exception.BusinessException;

import java.util.UUID;

public class CommonTools {
    // 获取uuid
    public static String getUUID() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    // 解密
    public static String decryptToken(String token) {
        String jwt;
        try {
            Claims claims = JwtUtils.parseJWT(token);
            jwt = claims.getSubject();
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException("token非法");
        }
        return jwt;
    }
}
