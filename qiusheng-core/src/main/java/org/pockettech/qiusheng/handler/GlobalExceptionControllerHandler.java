package org.pockettech.qiusheng.handler;

import org.pockettech.qiusheng.entity.result.CommonResult;
import org.pockettech.qiusheng.exception.BusinessException;
import org.springframework.dao.QueryTimeoutException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

/**
 * 全局异常处理，将异常的返回值封装成对象
 */
@RestControllerAdvice
public class GlobalExceptionControllerHandler {

    @ExceptionHandler(value = BusinessException.class)
    public CommonResult<String> businessExceptionHandler(HttpServletRequest request, Exception ex) {
        ex.printStackTrace();
        return new CommonResult<>(400, ex.getMessage());
    }

    //redis连接超时
    @ExceptionHandler(value = QueryTimeoutException.class)
    public CommonResult<String> queryTimeoutExceptionHandler(HttpServletRequest request, Exception ex) {
        ex.printStackTrace();
        return new CommonResult<>(500, "数据库连接异常，请稍后再试");
    }

    //权限不足
    @ExceptionHandler(value = AccessDeniedException.class)
    public CommonResult<String> accessDeniedExceptionHandler(HttpServletRequest request, Exception ex) {
        ex.printStackTrace();
        return new CommonResult<>(401, "您的权限不足");
    }

    //其他未知的异常捕获
    @ExceptionHandler(value = Exception.class)
    public CommonResult<String> exceptionHandler(HttpServletRequest request, Exception ex){
        ex.printStackTrace();
        return new CommonResult<>(500, "未知错误");
    }
}
