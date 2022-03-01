package org.pockettech.qiusheng.entity.filter;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
//这是查询铺面用的参数
//可删除
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChartFilter {
    private int sid;
    private int cid;
    private int org;
    private int beta;
    private int mode;
    private int from;
    private int promote;
}
