package org.pockettech.qiusheng.entity.charttransfer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//无用，待删除
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChartMsg {
    private int sid;
    private int cid;
    private String name;
    private String hash;
    private int size;
    private String main;
}
