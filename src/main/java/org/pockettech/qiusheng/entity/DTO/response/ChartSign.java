package org.pockettech.qiusheng.entity.DTO.response;

import com.alibaba.fastjson2.JSONObject;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChartSign {
    private int code;

    private int errorIndex;

    private String errorMsg;

    private String host;

    private List<JSONObject> meta;
}
