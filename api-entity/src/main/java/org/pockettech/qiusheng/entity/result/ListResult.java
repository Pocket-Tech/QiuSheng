package org.pockettech.qiusheng.entity.result;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

//这是所有列表对象的返回类型
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ListResult<T> {
    private int code;
    private boolean hasMore;
    private int next;
    private List<T> data;
}
