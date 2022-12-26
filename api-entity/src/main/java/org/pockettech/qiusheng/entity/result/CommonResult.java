package org.pockettech.qiusheng.entity.result;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommonResult<T> {
    // 状态码
    private Integer code;

    // 响应信息
    private String message;

    // 结果
    private T result;

    public CommonResult(Integer code,String message){
        this(code,message,null);
    }

}