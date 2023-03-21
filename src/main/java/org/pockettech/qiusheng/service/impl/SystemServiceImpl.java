package org.pockettech.qiusheng.service.impl;

import org.pockettech.qiusheng.cache.CacheData;
import org.pockettech.qiusheng.constant.ResultCode;
import org.pockettech.qiusheng.constant.SystemConfig;
import org.pockettech.qiusheng.entity.Admin;
import org.pockettech.qiusheng.exception.BusinessException;
import org.pockettech.qiusheng.service.SystemService;
import org.pockettech.qiusheng.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class SystemServiceImpl implements SystemService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Override
    public String login(Admin admin) {
        return authentication(admin);
    }

    /**
     * 认证并处理token
     * @param admin 登录用户信息
     * @return
     */
    public String authentication(Admin admin) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(admin.getUsername(), admin.getPassword());
        Authentication authentication = authenticationManager.authenticate(authenticationToken);

        if (Objects.isNull(authentication)) {
            throw new BusinessException(ResultCode.LOGIN_FAILED);
        }
        // 认证通过，生成token，并且存入redis
        Admin loginUser = (Admin) authentication.getPrincipal();

        String jwt = JwtUtils.createJWT(loginUser.getId().toString());

        Map<String, Object> map = new HashMap<>();
        // 添加角色
        map.put("role", loginUser.getRole());
        map.put("userName", loginUser.getUsername());
        map.put("jwt", jwt);
        map.put("locked", loginUser.getIsLocked());

        // 计算token失效时间
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.HOUR, -SystemConfig.LOGIN_TIME);
        map.put("timeOut", calendar.getTime());

        CacheData.tokenCache.put(loginUser.getId().toString(), map);

        return jwt;
    }
}
