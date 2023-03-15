package org.pockettech.qiusheng.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Download {
    private String name;

    private String hash;

    private String file;
}
