package org.pockettech.qiusheng.handler;

import org.pockettech.qiusheng.entity.data.User;
import org.pockettech.qiusheng.entity.userrole.Admin;
import org.pockettech.qiusheng.exception.BusinessException;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 重写security认证
 */
@Component
public class MyAuthenticationProvider implements AuthenticationProvider {

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = (String) authentication.getPrincipal();
        String password = (String) authentication.getCredentials();

        //TODO:查找数据库比较密码
        User user = new User();

        if (ObjectUtils.isEmpty(user)) {
            throw new BusinessException("用户名或密码错误");
        }

        List<String> list = new ArrayList<>();

        //TODO:添加权限
        list.add(null);

        UserDetails userDetails = new Admin(user, list);

        // 判断用户密码是否一致
        if (user.getPassword() == null || !user.getPassword().equals(DigestUtils.md5DigestAsHex(password.getBytes()))) {
            throw new BusinessException("用户名或密码错误");
        }

        Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();

        return new UsernamePasswordAuthenticationToken(userDetails, password, authorities);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return true;
    }
}
