package org.pockettech.qiusheng.impl;

import lombok.extern.slf4j.Slf4j;
import org.pockettech.qiusheng.entity.Data.Song;
import org.pockettech.qiusheng.api.StoreListService;
import org.pockettech.qiusheng.dao.SongDao;
import org.pockettech.qiusheng.entity.Result.ListResult;
import org.pockettech.qiusheng.entity.Result.SongResult;
import org.pockettech.qiusheng.entity.builder.StoreConditionalFilterBuilder;
import org.pockettech.qiusheng.entity.filter.StoreConditionalFilter;
import org.pockettech.qiusheng.entity.tools.SongFilterHandle;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

//这里是返回歌曲列表的服务,以及推荐列表的服务
@RestController
@Slf4j
public class StoreListServiceImpl implements StoreListService {
    @Resource
    SongDao songDao;

    StoreConditionalFilterBuilder builder = new StoreConditionalFilterBuilder();
//  歌曲列表
    @GetMapping("/api/store/list")
    @ResponseBody
    public ListResult<SongResult> returnList(@RequestParam(required = false) String word,
                                             @RequestParam(required = false) Integer org,
                                             @RequestParam(required = false) Integer mode,
                                             @RequestParam(required = false) Integer lvge,
                                             @RequestParam(required = false) Integer lvle,
                                             @RequestParam(required = false) Integer beta,
                                             @RequestParam(required = false) Integer from) {
        StoreConditionalFilter filter = builder.constructListFilter(word, org, mode, lvge, lvle, beta, from);
        log.info("‘/api/store/list’调用,正在查询歌曲列表...");

        from = (from == null ? 0 : from);
        int count = songDao.findCount();
        List<Song> songs;
        boolean hasmore = (from + 50) < count;
        songs = songDao.returnSomeSongList(from, 50);

        if (mode != null && mode != -1) {
            SongFilterHandle handle = new SongFilterHandle(filter);
            songs = handle.getSongsByMode(songs);
        }

        List<SongResult> result = new ArrayList<>();
        for (Song song:songs){
            SongResult songResult = new SongResult(song);
            result.add(songResult);
        }

        log.info("已完成歌曲列表查询");

        return new ListResult<>(1, hasmore,from + 50, result);
    }

    @Override
    public List<Song> returnSongList() {
        List<Song> songs = songDao.returnSongList();
        return songs;
    }

    //  推荐列表
    @GetMapping("/api/store/promote")
    @ResponseBody
    public ListResult<SongResult> storePromote(@RequestParam(required = false) Integer org,
                                         @RequestParam(required = false) Integer mode,
                                         @RequestParam(required = false) Integer from){
        StoreConditionalFilter filter = builder.constructPromoteFilter(org, mode, from);

        log.info("‘/api/store/promote’调用,正在查询推荐列表...");

        List<Song> songs = songDao.findSongByCMode(mode);
        List<SongResult> result = new ArrayList<>();
        for (Song song:songs){
            SongResult songResult = new SongResult(song);
            result.add(songResult);
        }

        log.info("已完成推荐列表查询");

        return new ListResult<>(1,false,0, result);
    }

}
