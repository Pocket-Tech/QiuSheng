package org.pockettech.qiusheng.entity.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Song implements Serializable {
    private int sid;
    private int length;
    private int bpm;
    private String title;
    private String artist;
    private int s_mode;
    private int time;
    private String s_md5;
    private String s_file_path;
    private String img_md5;
    private String img_file_path;
}
