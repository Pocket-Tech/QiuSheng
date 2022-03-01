package org.pockettech.qiusheng.entity.filter;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//这是谱面筛选的传参
//可删除
@Data
@AllArgsConstructor
@NoArgsConstructor
public class StoreConditionalFilter {
    //    歌曲id
    private int sid;
    //    谱面id
    private int cid;
    //    搜索关键词
    private String word;
    //    是否返回原始标题
    private int org;
    //    返回指定模式谱面
    private int mode;
    //    返回level大于于这个值的谱面
    private int lvge;
    //    返回level小于这个值的谱面
    private int lvle;
    //    返回非stable谱面
    private int beta;
    //    翻页起点
    private int from;

}
