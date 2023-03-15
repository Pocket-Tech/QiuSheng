package org.pockettech.qiusheng.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Information {
    private int code = 0;

    private int api = 202206;

    private int min = 202206;

    private String welcome = "Welcome to QiuSheng Server";
}
