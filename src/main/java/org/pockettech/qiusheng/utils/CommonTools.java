package org.pockettech.qiusheng.utils;

import io.jsonwebtoken.Claims;
import org.pockettech.qiusheng.exception.BusinessException;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

public class CommonTools {
    // 获取uuid
    public static String getUUID() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    // 解密
    public static String decryptToken(String token) throws BusinessException {
        String jwt;
        try {
            Claims claims = JwtUtils.parseJWT(token);
            jwt = claims.getSubject();
        } catch (Exception e) {
            throw new BusinessException("token非法");
        }
        return jwt;
    }

    /**
     * 获取String参数
     */
    public static String getParameter(String name) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        assert attributes != null;
        return attributes.getRequest().getParameter(name);
    }

    /**
     * 获取Boolean参数
     */
    public static Boolean getParameterToBool(String name) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        assert attributes != null;
        return Convert.toBool(attributes.getRequest().getParameter(name));
    }

    /**
     * 驼峰转下划线命名
     */
    public static String toUnderScoreCase(String str) {
        if (str == null)
        {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        // 前置字符是否大写
        boolean preCharIsUpperCase = true;
        // 当前字符是否大写
        boolean curreCharIsUpperCase = true;
        // 下一字符是否大写
        boolean nexteCharIsUpperCase = true;
        for (int i = 0; i < str.length(); i++)
        {
            char c = str.charAt(i);
            if (i > 0)
            {
                preCharIsUpperCase = Character.isUpperCase(str.charAt(i - 1));
            }
            else
            {
                preCharIsUpperCase = false;
            }

            curreCharIsUpperCase = Character.isUpperCase(c);

            if (i < (str.length() - 1))
            {
                nexteCharIsUpperCase = Character.isUpperCase(str.charAt(i + 1));
            }

            if (preCharIsUpperCase && curreCharIsUpperCase && !nexteCharIsUpperCase)
            {
                sb.append("_");
            }
            else if ((i != 0 && !preCharIsUpperCase) && curreCharIsUpperCase)
            {
                sb.append("_");
            }
            sb.append(Character.toLowerCase(c));
        }

        return sb.toString();
    }
}
