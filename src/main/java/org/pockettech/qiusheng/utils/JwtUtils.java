package org.pockettech.qiusheng.utils;

import io.jsonwebtoken.*;
import org.pockettech.qiusheng.constant.SystemConfig;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;
import java.util.Date;

/**
 * JWT工具类
 */
public class JwtUtils {
    /**
     * 生成JWT
     * @param subject token中要存放的数据（json格式）
     * @return JWT
     */
    public static String createJWT(String subject) {
        JwtBuilder builder = getJwtBuilder(subject, null, CommonTools.getUUID());
        return builder.compact();
    }

    /**
     * 创建token
     * @param id 标识
     * @param subject token中要存放的数据（json格式）
     * @param ttlMillis 有效时间
     * @return JWT
     */
    public static String createJWT(String id, String subject, Long ttlMillis) {
        JwtBuilder builder = getJwtBuilder(subject, ttlMillis, id);
        return builder.compact();
    }

    private static JwtBuilder getJwtBuilder(String subject, Long ttlMillis, String uuid) {
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        SecretKey secretKey = generalKey();
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
        if (ttlMillis == null) {
            ttlMillis = SystemConfig.JWT_TTL;
        }
        long expMillis = nowMillis + ttlMillis;
        Date expDate = new Date(expMillis);
        return Jwts.builder()
                .setId(uuid) //唯一id
                .setSubject(subject) // 主体
                .setIssuer("tx") // 签发者
                .setIssuedAt(now) // 签发时间
                .signWith(signatureAlgorithm, secretKey) // 使用HS256对称加密算法签名，第二个参数为秘钥
                .setExpiration(expDate);
    }

    /**
     * 生成加密后的秘钥SecretKey
     * @return 加密后的秘钥SecretKey
     */
    public static SecretKey generalKey() {
        byte[] encodeedKey = Base64.getDecoder().decode(SystemConfig.JWT_KEY);
        return new SecretKeySpec(encodeedKey, 0, encodeedKey.length, "AES");
    }

    /**
     * 解析
     * @param jwt token
     */
    public static Claims parseJWT(String jwt) {
        SecretKey secretKey = generalKey();
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(jwt)
                .getBody();
    }

}
