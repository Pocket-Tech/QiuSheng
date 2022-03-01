package org.pockettech.qiusheng.entity.Result;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.pockettech.qiusheng.entity.Data.Chart;
import org.pockettech.qiusheng.entity.Data.Song;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChartResult {
    private int cid;
    private int sid; //song
    private int uid;
    private String creator; //user
    private String version;
    private String title; //song
    private String artist; //song
    private int level;
    private int length; //song
    private int type;
    private String cover; //song
    private long size;
    private int mode;
    private String file_path;

    public ChartResult(Chart chart, Song song, String creator){
         this.cid = chart.getCid();
         this.sid = song.getSid();
         this.uid = chart.getUid();
         this.creator = creator;
         this.version = chart.getVersion();
         this.title = song.getTitle();
         this.artist = song.getArtist();
         this.level = chart.getLevel();
         this.length = song.getLength();
         this.type = chart.getType();
         this.cover = song.getImg_file_path();
         this.size = chart.getSize();
         this.mode = chart.getC_mode();
         this.file_path = chart.getC_file_path();
    }
}
