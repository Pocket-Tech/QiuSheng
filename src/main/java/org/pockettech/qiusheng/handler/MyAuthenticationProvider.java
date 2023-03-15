package org.pockettech.qiusheng.handler;

import org.pockettech.qiusheng.entity.Admin;
import org.pockettech.qiusheng.exception.BusinessException;
import org.pockettech.qiusheng.mapper.AdminMapper;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.Collection;

/**
 * 重写security认证
 */
@Component
public class MyAuthenticationProvider implements AuthenticationProvider {

    @Resource
    private AdminMapper adminMapper;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = (String) authentication.getPrincipal();
        String password = (String) authentication.getCredentials();

        //TODO:查找数据库比较密码
        Admin admin = adminMapper.getAdminByUsername(username);

        if (ObjectUtils.isEmpty(admin)) {
            throw new BusinessException("Username or password error");
        }

        UserDetails userDetails = new Admin(admin.getId(), username, admin.getPassword(), admin.getRole());

        // 判断用户密码是否一致
        if (admin.getPassword() == null || !admin.getPassword().equals(DigestUtils.md5DigestAsHex(password.getBytes()))) {
            throw new BusinessException("Username or password error");
        }

        Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();

        return new UsernamePasswordAuthenticationToken(userDetails, password, authorities);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return true;
    }
}
