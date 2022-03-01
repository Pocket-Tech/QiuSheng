package org.pockettech.qiusheng.entity.charttransfer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
//文件名,文件md5值,文件的下载地址url
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Download {
    private String name;
    private String hash;
    private String file;
}
