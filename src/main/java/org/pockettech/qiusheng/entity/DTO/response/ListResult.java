package org.pockettech.qiusheng.entity.DTO.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

//这是所有列表对象的返回类型
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ListResult<T> extends CommonResult<T> {
    private Boolean hasMore;
    private Integer next;

    public ListResult(Integer code, Boolean hasMore, Integer next, T data) {
        super(code, data);
        this.hasMore = hasMore;
        this.next = next;
    }

    public static ListResult<Object> success() {
        return (ListResult<Object>) ListResult.success(new ArrayList<>());
    }

    public static ListResult<Object> success(List<Object> data) {
        return ListResult.success(1, data);
    }

    public static ListResult<Object> success(Integer next, List<Object> data) {
        return ListResult.success(false, 1, data);
    }

    public static ListResult<Object> success(Boolean hasMore, Integer next, List<Object> data) {
        return new ListResult<>(0, hasMore, next, data);
    }

    public static ListResult<Object> error() {
        return new ListResult<>(1, false, 1, null);
    }

}
