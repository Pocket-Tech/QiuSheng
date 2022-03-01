package org.pockettech.qiusheng.entity.Result;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ActivityResult {
    private int eid;
    private String name;
    private String sponsor;
    private String start;
    private String end;
    private boolean cover;
    private String active;
}
