package org.pockettech.qiusheng.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.pockettech.qiusheng.entity.DTO.response.SongResult;
import org.pockettech.qiusheng.entity.Song;

import java.util.List;

@Mapper
public interface SongMapper {
    public List<SongResult> selectSongResultList(@Param("cid") Integer cid, @Param("sid") Integer sid);

    public List<SongResult> selectSongList(@Param("mode") Integer mode, @Param("lvge") Integer lvge, @Param("lvle") Integer lvle, @Param("promote") Integer promote);

    public Song selectSongById(@Param("sid") Integer sid);

    public void updateSong(Song song);
}
