package org.pockettech.qiusheng.entity.Data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Chart implements Serializable {
    private int cid;
    private int sid;
    private int uid;
    private String version;
    private int level;
    private int type;
    private long size;
    private int c_mode;
    private String c_md5;
    private String c_file_path;
    private Song song;
    private User user;
}
