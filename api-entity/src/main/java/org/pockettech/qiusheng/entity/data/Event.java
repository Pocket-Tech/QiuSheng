package org.pockettech.qiusheng.entity.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Event {
    private int eid;
    private String name;
    private String sponsor;
    private String end;
    private String cover;
    private int active;
    private String cid_list;
}
