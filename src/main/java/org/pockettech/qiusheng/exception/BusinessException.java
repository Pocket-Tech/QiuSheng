package org.pockettech.qiusheng.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.pockettech.qiusheng.constant.ResultMessageEnum;

/**
 * 业务异常
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class BusinessException extends RuntimeException {

    // 状态码
    private int code;

    // 异常信息
    private String message;

    public BusinessException() {
        this.code = ResultMessageEnum.UnknownError.getCode();
        this.message = ResultMessageEnum.UnknownError.getMessage();
    }

    public BusinessException(String message) {
        this.code = ResultMessageEnum.ERROR.getCode();
        this.message = message;
    }

    public BusinessException(int code) {
        this.code = code;
    }

    public BusinessException(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
