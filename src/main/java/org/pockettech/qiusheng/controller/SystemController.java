package org.pockettech.qiusheng.controller;

import org.pockettech.qiusheng.constant.SystemConfig;
import org.pockettech.qiusheng.entity.DTO.response.CommonResult;
import org.pockettech.qiusheng.entity.Information;
import org.pockettech.qiusheng.exception.BusinessException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
public class SystemController {

    Information info = new Information();

    /**
     * 获取admin登录欢迎语
     */
    @GetMapping("/admin/info")
    public Information returnAdminInfo() {
        info.setWelcome(SystemConfig.ADMIN_WELCOME);
        return info;
    }

    /**
     * 登录失败
     */
    @PostMapping("/admin/error")
    public void loginError() {
        throw new LoginException();
    }

    /**
     * 全局异常处理无法捕获的地方转发异常信息到此供全局异常处理捕获
     */
    @RequestMapping("/throwError")
    public CommonResult<Object> error(HttpServletRequest request) {
        throw new BusinessException((String) request.getAttribute("exception"));
    }

    /**
     * 登录失败
     */
    @ResponseStatus(value = HttpStatus.BAD_REQUEST,reason = "Login Failure")
    public static class LoginException extends RuntimeException {
    }
}
