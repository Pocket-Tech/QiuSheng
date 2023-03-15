package org.pockettech.qiusheng.service;

import org.apache.ibatis.annotations.Param;
import org.pockettech.qiusheng.entity.DTO.response.SongResult;
import org.pockettech.qiusheng.entity.Song;

import java.util.List;

public interface StoreSongService {

    public List<SongResult> selectSongResultList(Integer cid, Integer sid);

    public List<SongResult> selectSongList(Integer mode, Integer lvge, Integer lvle, Integer promote);

    public Song selectSongById(Integer sid);

    public void updateSong(Song song);
}
