package org.pockettech.qiusheng.handler;

import com.alibaba.fastjson.JSON;
import org.pockettech.qiusheng.entity.result.CommonResult;
import org.pockettech.qiusheng.utils.WebUtils;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 登录失败
 */
@Component("LoginFailureHandler")
public class LoginFailureHandler implements AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        response.setHeader("Content-Type", "text/html;charset=UTF-8");
        response.setStatus(400);
        CommonResult<String> commonResult = new CommonResult<>(400, "登录失败");
        String json = JSON.toJSONString(commonResult);
        WebUtils.renderString(response, json);
    }
}
