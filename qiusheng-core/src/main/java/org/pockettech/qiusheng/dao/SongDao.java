package org.pockettech.qiusheng.dao;

import org.apache.ibatis.annotations.Param;
import org.pockettech.qiusheng.entity.data.Chart;
import org.pockettech.qiusheng.entity.data.Song;

import java.util.List;

public interface SongDao {
    int findCount();
    List<Song> returnSongList();
    List<Song> returnSomeSongList(@Param("first") int first,@Param("number") int number);
    List<Song> findSongByCMode(@Param("mode") int mode);
    Song findSongById(@Param("sid") int sid);
    void updateSong(Song song);
    int deleteSong(@Param("sid") int sid);
}
