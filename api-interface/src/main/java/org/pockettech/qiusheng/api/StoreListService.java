package org.pockettech.qiusheng.api;

import org.pockettech.qiusheng.entity.Data.Song;
import org.pockettech.qiusheng.entity.Result.ListResult;
import org.pockettech.qiusheng.entity.Result.SongResult;

import java.util.List;

public interface StoreListService {
    ListResult<SongResult> returnList(String word, Integer org, Integer mode, Integer lvge, Integer lvle, Integer beta, Integer from);
    List<Song> returnSongList();
    ListResult<SongResult> storePromote(Integer org, Integer mode, Integer from);
}
