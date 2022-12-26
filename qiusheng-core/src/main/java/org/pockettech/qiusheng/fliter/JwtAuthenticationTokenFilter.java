package org.pockettech.qiusheng.fliter;

import io.jsonwebtoken.Claims;
import org.pockettech.qiusheng.entity.userrole.Admin;
import org.pockettech.qiusheng.exception.BusinessException;
import org.pockettech.qiusheng.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

/**
 * 用于验证token合法性的过滤器
 */
@Component
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String token = request.getHeader("token");
        // 对没有token的请求进行放行，此为访问无需认证的接口
        if (!StringUtils.hasText(token)) {
            filterChain.doFilter(request, response);
            return;
        }

        String userid;
        //解密token
        try {
            Claims claims = JwtUtils.parseJWT(token);
            userid = claims.getSubject();
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException("token非法");
        }

        Admin admin = (Admin) redisTemplate.opsForValue().get(userid);
        if (Objects.isNull(admin)) {
            throw new BusinessException("用户未登录");
        }
        // 认证
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(admin, null, admin.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        filterChain.doFilter(request, response);
    }
}
