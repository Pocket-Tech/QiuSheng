package org.pockettech.qiusheng.fliter;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.TypeReference;
import io.jsonwebtoken.Claims;
import org.pockettech.qiusheng.cache.CacheData;
import org.pockettech.qiusheng.constant.ResultCode;
import org.pockettech.qiusheng.constant.SystemConfig;
import org.pockettech.qiusheng.entity.Admin;
import org.pockettech.qiusheng.utils.JwtUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

/**
 * 用于验证token的过滤器
 */
@Component
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String token = request.getHeader("token");
        // 对没有token的请求进行放行
        if (!StringUtils.hasText(token)) {
            filterChain.doFilter(request, response);
            return;
        }

        int userId;

        //解密token
        try {
            Claims claims = JwtUtils.parseJWT(token);
            userId = Integer.parseInt(claims.getSubject());
        } catch (Exception e) {
            // token非法
            // 转发异常到controller层，让全局异常处理可以正常捕获
            request.setAttribute("code", ResultCode.ILLEGAL_TOKEN);
            request.getRequestDispatcher("/throwError").forward(request, response);
            return;
        }

        Object cache = CacheData.tokenCache.get(Integer.toString(userId));
        JSONObject jsonAdmin = JSONObject.parseObject(JSON.toJSONString(cache));

        if (ObjectUtils.isEmpty(jsonAdmin)) {
            // 登录状态失效
            // 转发异常到controller层，让全局异常处理可以正常捕获
            request.setAttribute("code", ResultCode.LOGIN_EXPIRED);
            request.getRequestDispatcher("/throwError").forward(request, response);
            return;
        }

        String userName = jsonAdmin.getString("userName");
        String roles = jsonAdmin.getString("role");
        Boolean locked = (Boolean) jsonAdmin.get("locked");

        if (locked) {
            // 账户被锁定
            // 转发异常到controller层，让全局异常处理可以正常捕获
            request.setAttribute("code", ResultCode.ACCOUNT_LOCKED);
            request.getRequestDispatcher("/throwError").forward(request, response);
            return;
        }

        Admin admin = new Admin(userId, userName, roles, false);

        // 添加权限
        List<String> permissions = new ArrayList<>();
        if (admin.getRole() != null && !"".equals(admin.getRole())) {
            permissions = Arrays.asList(admin.getRole().split(","));
        }
        admin.setPermissions(permissions);

        // 认证
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(admin, null, admin.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        // 刷新登录状态
        Map<String, Object> map = JSONObject.parseObject(jsonAdmin.toString(), new TypeReference<Map<String, Object>>(){});

        // 计算token失效时间
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.HOUR, -SystemConfig.LOGIN_TIME);
        map.put("timeOut", calendar.getTime());

        CacheData.tokenCache.put(Integer.toString(userId), map);

        filterChain.doFilter(request, response);

    }
}
