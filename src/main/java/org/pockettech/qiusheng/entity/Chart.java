package org.pockettech.qiusheng.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Chart implements Serializable {
    /** 谱面id */
    private Integer cid;

    /** 歌曲id */
    private Integer sid;

    /** 作者id */
    private Integer uid;

    /** 谱面难度名 */
    private String version;

    /** 难度等级 */
    private Integer level;

    /** 谱面状态，2代表Stable，1代表Beta，0代表Alpha */
    private Integer type;

    /** 谱面下载大小，单位：字节 */
    private Long size;

    /** 谱面模式 */
    private Integer mode;

    /** 谱面文件的md5值（文件校验） */
    private String c_md5;

    /** 普面文件路径 */
    private String c_file_path;

    /** 是否推荐 */
    private Integer promote;

    /** 是否稳定 */
    private Integer beta;

    /** 外部属性 谱面歌曲信息 */
    private Song song;

    /** 外部属性 谱面作者信息 */
    private User user;

    public Chart(String hashCode) {
        this.c_md5 = hashCode;
    }
}
