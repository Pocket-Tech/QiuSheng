package org.pockettech.qiusheng.handler;

import lombok.extern.slf4j.Slf4j;
import org.pockettech.qiusheng.constant.ResultCode;
import org.pockettech.qiusheng.constant.ResultMessageEnum;
import org.pockettech.qiusheng.entity.DTO.response.CommonResult;
import org.pockettech.qiusheng.exception.BusinessException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import java.sql.SQLException;

/**
 * 全局异常处理，将异常的返回值封装成对象
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionControllerHandler {
    /**
     * 普通业务异常
     */
    @ExceptionHandler(value = BusinessException.class)
    public CommonResult<Object> businessExceptionHandler(HttpServletRequest request, BusinessException ex) {
        if (ex.getMessage() == null || ex.getMessage().equals("")) {
            log.error(ResultMessageEnum.getMessage(ex.getCode()));
            return CommonResult.error(ex.getCode());
        } else {
            log.error(ex.getMessage());
            return CommonResult.error(ex.getCode(), ex.getMessage());
        }
    }

    /**
     * 数据库异常
     */
    @ExceptionHandler(value = SQLException.class)
    public CommonResult<Object> SQLExceptionHandler(HttpServletRequest request, Exception ex) {
        log.error(ex.getMessage());
        return CommonResult.error(ResultCode.ERROR, "数据库异常");
    }

    /**
     * 权限不足
     */
    @ExceptionHandler(value = AccessDeniedException.class)
    public CommonResult<Object> accessDeniedExceptionHandler(HttpServletRequest request, Exception ex) {
        log.error(ex.getMessage());
        return CommonResult.error(ResultCode.FORBIDDEN);
    }

    /**
     * 其他未知的异常处理
     */
    @ExceptionHandler(value = Exception.class)
    public CommonResult<Object> exceptionHandler(HttpServletRequest request, Exception ex){
        log.error(ex.getMessage());
        ex.printStackTrace();
        return CommonResult.error(600);
    }
}
