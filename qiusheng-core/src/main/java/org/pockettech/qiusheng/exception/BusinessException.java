package org.pockettech.qiusheng.exception;

import lombok.Data;

/**
 * 业务异常
 */
@Data
public class BusinessException extends RuntimeException {

    //业务异常code为400
    private static final int DEFAULT_CODE = 400;

    private int code;
    private String message;

    public BusinessException(String message) {
        this.code = DEFAULT_CODE;
        this.message = message;
    }


}