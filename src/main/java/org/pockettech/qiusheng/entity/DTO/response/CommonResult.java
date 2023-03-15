package org.pockettech.qiusheng.entity.DTO.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.pockettech.qiusheng.constant.ResultCode;
import org.pockettech.qiusheng.constant.ResultMessageEnum;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommonResult<T> {

    // 状态码
    private Integer code;

    // 响应信息
    private String message;

    // 结果
    private T data;

    public CommonResult(Integer code, String message){
        this(code,message,null);
    }

    public CommonResult(Integer code, T data) {
        this(code,null,data);
    }

    public static CommonResult<Object> success() {
        return CommonResult.success(ResultMessageEnum.SUCCESS.getMessage());
    }

    public static CommonResult<Object> success(String message) {
        return CommonResult.success(message, null);
    }

    public static CommonResult<Object> success(Object data) {
        return CommonResult.success(ResultMessageEnum.SUCCESS.getMessage(), data);
    }

    public static CommonResult<Object> success(String msg, Object data) {
        return new CommonResult<>(ResultCode.SUCCESS, msg, data);
    }

    public static CommonResult<Object> error() {
        return CommonResult.error(ResultMessageEnum.ERROR.getMessage());
    }

    public static CommonResult<Object> error(int code) {
        return CommonResult.error(code, ResultMessageEnum.getMessage(code));
    }

    public static CommonResult<Object> error(String msg) {
        return CommonResult.error(msg, null);
    }

    public static CommonResult<Object> error(String msg, Object data) {
        return new CommonResult<>(ResultCode.ERROR, msg, data);
    }

    public static CommonResult<Object> error(int code, String msg) {
        return new CommonResult<>(code, msg, null);
    }

}