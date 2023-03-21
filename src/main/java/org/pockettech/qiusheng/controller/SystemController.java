package org.pockettech.qiusheng.controller;

import org.pockettech.qiusheng.constant.SystemConfig;
import org.pockettech.qiusheng.entity.Admin;
import org.pockettech.qiusheng.entity.DTO.response.CommonResult;
import org.pockettech.qiusheng.entity.Information;
import org.pockettech.qiusheng.exception.BusinessException;
import org.pockettech.qiusheng.service.SystemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
public class SystemController {

    @Autowired
    private SystemService systemService;

    Information info = new Information();

    /**
     * 登录
     */
    @PostMapping("/admin/login")
    public CommonResult<?> login(@RequestParam("userName") String userName,
                                 @RequestParam("password") String password) {
        String token = systemService.login(new Admin(userName, password));
        return CommonResult.success("登录成功！", token);
    }

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
        throw new BusinessException((Integer) request.getAttribute("code"));
    }

    /**
     * 登录失败
     */
    @ResponseStatus(value = HttpStatus.BAD_REQUEST,reason = "Login Failure")
    public static class LoginException extends RuntimeException {
    }
}
