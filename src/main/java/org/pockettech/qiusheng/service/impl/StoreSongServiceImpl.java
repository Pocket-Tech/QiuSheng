package org.pockettech.qiusheng.service.impl;

import org.pockettech.qiusheng.entity.DTO.response.SongResult;
import org.pockettech.qiusheng.entity.Song;
import org.pockettech.qiusheng.mapper.SongMapper;
import org.pockettech.qiusheng.service.StoreSongService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class StoreSongServiceImpl implements StoreSongService {

    @Autowired
    private SongMapper songMapper;

    @Override
    public List<SongResult> selectSongResultList(Integer cid, Integer sid) {
        return songMapper.selectSongResultList(cid, sid);
    }

    @Override
    public List<SongResult> selectSongList(Integer mode, Integer lvge, Integer lvle, Integer promote) {
        return songMapper.selectSongList(mode, lvge, lvle, promote);
    }

    @Override
    public Song selectSongById(Integer sid) {
        return songMapper.selectSongById(sid);
    }

    @Override
    public void updateSong(Song song) {
        songMapper.updateSong(song);
    }

    public List<Song> getSongsByMode(List<Song> songs, Integer mode) {
        List<Song> result = new ArrayList<>();
        for (Song song: songs) {
            if ((song.getS_mode() | (1 << mode)) == song.getS_mode())
                result.add(song);
        }
        return result;
    }
}
