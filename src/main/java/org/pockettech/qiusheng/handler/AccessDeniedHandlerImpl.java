package org.pockettech.qiusheng.handler;

import com.alibaba.fastjson2.JSON;
import org.pockettech.qiusheng.constant.ResultCode;
import org.pockettech.qiusheng.entity.DTO.response.CommonResult;
import org.pockettech.qiusheng.utils.WebUtils;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * security用户权限不足处理
 */
@Component
public class AccessDeniedHandlerImpl implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        String json = JSON.toJSONString(CommonResult.error(ResultCode.FORBIDDEN));
        WebUtils.renderString(response, json);
    }
}