package org.pockettech.qiusheng.handler;

import com.alibaba.fastjson2.JSON;
import org.pockettech.qiusheng.constant.ResultCode;
import org.pockettech.qiusheng.entity.DTO.response.CommonResult;
import org.pockettech.qiusheng.utils.WebUtils;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * security用户登录认证失败处理
 */
@Component
public class AuthenticationEntryPointImpl implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        String json = JSON.toJSONString(CommonResult.error(ResultCode.UNAUTHORIZED));
        WebUtils.renderString(response, json);
    }
}
