package org.pockettech.qiusheng.entity.result;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.pockettech.qiusheng.entity.data.Song;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SongResult {
    private int sid;
    private String cover; //img
    private int length;
    private int bpm;
    private String title;
    private String artist;
    private int mode;
    private int time;

    public SongResult(Song song){
        this.sid = song.getSid();
        this.cover = song.getImg_file_path();
        this.length = song.getLength();
        this.bpm = song.getBpm();
        this.title = song.getTitle();
        this.artist = song.getArtist();
        this.mode = song.getS_mode();
        this.time = song.getTime();
    }
}
