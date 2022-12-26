package org.pockettech.qiusheng.handler;

import com.alibaba.fastjson.JSON;
import org.pockettech.qiusheng.entity.result.CommonResult;
import org.pockettech.qiusheng.utils.WebUtils;
import org.springframework.http.HttpStatus;
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
        CommonResult<String> commonResult = new CommonResult<>(HttpStatus.UNAUTHORIZED.value(), "认证失败");
        String json = JSON.toJSONString(commonResult);
        WebUtils.renderString(response, json);
    }
}
