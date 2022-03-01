package org.pockettech.qiusheng.entity.charttransfer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MetaMsg {
    int sid, cid;
    String name, hash;
}