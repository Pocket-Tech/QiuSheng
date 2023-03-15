package org.pockettech.qiusheng.entity.DTO.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChartResult {
    private Integer cid;
    private Integer sid; //song
    private Integer uid;
    private String creator; //user
    private String version;
    private String title; //song
    private String artist; //song
    private Integer level;
    private Integer length; //song
    private Integer type;
    private String cover; //song
    private Long size;
    private Integer mode;
    private String file_path;
    private Integer time;
}

