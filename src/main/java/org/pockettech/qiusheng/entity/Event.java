package org.pockettech.qiusheng.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Event {
    /** 活动id */
    private Integer eid;

    /** 活动名 */
    private String name;

    /** 发起人 */
    private String sponsor;

    /** 开始时间 */
    private String start;

    /** 结束时间 */
    private String end;

    /** 活动展示封面 */
    private String cover;

    /** 活动是否有效 */
    private boolean active;

    /** 活动下谱面列表 */
    private String cid_list;

}
