package org.pockettech.qiusheng.fliter;

import io.jsonwebtoken.Claims;
import org.pockettech.qiusheng.entity.Admin;
import org.pockettech.qiusheng.mapper.AdminMapper;
import org.pockettech.qiusheng.utils.JwtUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 用于验证token的过滤器
 */
@Component
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

    @Resource
    private AdminMapper adminMapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String token = request.getHeader("token");
        // 对没有token的请求进行放行，此为访问无需认证的接口
        if (!StringUtils.hasText(token)) {
            filterChain.doFilter(request, response);
            return;
        }

        String username;

        //解密token
        try {
            Claims claims = JwtUtils.parseJWT(token);
            username = claims.getSubject();
        } catch (Exception e) {
            // 转发异常到controller层，让全局异常处理可以正常捕获
            request.setAttribute("exception", "Illegal token");
            request.getRequestDispatcher("/throwError").forward(request, response);
            return;
        }

        Admin admin = adminMapper.getAdminByUsername(username);

        if (ObjectUtils.isEmpty(admin)) {
            // 转发异常到controller层，让全局异常处理可以正常捕获
            request.setAttribute("exception", "User not logged in");
            request.getRequestDispatcher("/throwError").forward(request, response);
            return;
        }

        // 添加权限
        List<String> permissions = new ArrayList<>();
        if (admin.getRole() != null && !"".equals(admin.getRole())) {
            permissions = Arrays.asList(admin.getRole().split(","));
        }
        admin.setPermissions(permissions);

        // 认证
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(admin, null, admin.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        filterChain.doFilter(request, response);

    }
}
