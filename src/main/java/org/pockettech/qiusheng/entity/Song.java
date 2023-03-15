package org.pockettech.qiusheng.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Song implements Serializable {

    private Integer sid;

    private Integer length;

    private Integer bpm;

    private String title;

    private String artist;

    private Integer s_mode;

    private Integer time;

    private String s_md5;

    private String s_file_path;

    private String img_md5;

    private String img_file_path;

    public Song(String s_md5, String img_md5) {
        this.s_md5 = s_md5;
        this.img_md5 = img_md5;
    }
}
