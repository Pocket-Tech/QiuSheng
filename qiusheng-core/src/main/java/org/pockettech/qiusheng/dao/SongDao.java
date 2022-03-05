package org.pockettech.qiusheng.dao;

import org.apache.ibatis.annotations.Param;
import org.pockettech.qiusheng.entity.data.Song;

import java.util.List;

public interface SongDao {
    int findCount();
    List<Song> returnSongList();
    List<Song> returnSomeSongList(@Param("first") int first,@Param("number") int number);
    List<Song> findSongByCMode(@Param("mode") int mode);
    List<Song> findSomeSongByCMode(@Param("mode") int mode, @Param("first") int first, @Param("number") int number);
    Song findSongById(@Param("sid") int sid);
    void uploadSongMsg(Song song);
    void updateSong(Song song);
    void updateLengthBySid(@Param("sid") int sid, @Param("length") int length);
    void updateSongMD5PathBySid(Song song);
}
