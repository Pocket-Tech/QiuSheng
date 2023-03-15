package org.pockettech.qiusheng.constant;

/**
 * 默认返回信息枚举类
 */
public enum ResultMessageEnum {

    //请求成功
    SUCCESS(ResultCode.SUCCESS,"Success"),

    // 请求参数错误
    BAD_REQUEST(ResultCode.BAD_REQUEST,"Parameter error"),

    // 未授权
    UNAUTHORIZED(ResultCode.UNAUTHORIZED,"Unauthorized"),

    // 权限不足
    FORBIDDEN(ResultCode.FORBIDDEN,"Access not allowed"),

    // 资源未找到或无法访问
    NOT_FOUND(ResultCode.NOT_FOUND,"Not found"),

    // 请求方式错误
    BAD_METHOD(ResultCode.BAD_METHOD,"Http method not allowed"),

    // 业务异常
    ERROR(ResultCode.ERROR,"System error"),

    // 文件处理失败
    FILE_PROCESSING_FAILED(ResultCode.FILE_PROCESSING_FAILED, "File processing failed"),

    // 签名校验失败
    SIGN_VERIFY_FAILED(ResultCode.SIGN_VERIFY_FAILED, "Signature verification failed"),

    // 文件丢失
    FILE_MISSING(ResultCode.FILE_MISSING, "file missing"),

    // 未知错误
    UnknownError(ResultCode.UnknownError, "Unknown error");


    private final Integer code;
    private final String message;

    ResultMessageEnum(Integer code, String message){
        this.code = code;
        this.message = message;
    }

    public static String getMessage(Integer code) {
        for (ResultMessageEnum messageEnum : ResultMessageEnum.values()) {
            if (messageEnum.getCode() == null) {
                return UnknownError.getMessage();
            }
            if (messageEnum.getCode().equals(code)) {
                return messageEnum.getMessage();
            }
        }
        return UnknownError.getMessage();
    }


    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

}
