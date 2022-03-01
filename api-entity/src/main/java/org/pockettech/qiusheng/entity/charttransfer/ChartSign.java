package org.pockettech.qiusheng.entity.charttransfer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChartSign {
    private int code;
    private int errorIndex;
    private String errorMsg;
    private String host;
    private List<MetaMsg> meta;
}
