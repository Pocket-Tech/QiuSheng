package org.pockettech.qiusheng.impl;

import lombok.extern.slf4j.Slf4j;
import org.pockettech.qiusheng.dao.ChartDao;
import org.pockettech.qiusheng.entity.data.Chart;
import org.pockettech.qiusheng.entity.data.Song;
import org.pockettech.qiusheng.api.StoreListService;
import org.pockettech.qiusheng.dao.SongDao;
import org.pockettech.qiusheng.entity.result.ListResult;
import org.pockettech.qiusheng.entity.result.SongResult;
import org.pockettech.qiusheng.entity.builder.StoreConditionalFilterBuilder;
import org.pockettech.qiusheng.entity.filter.StoreConditionalFilter;
import org.pockettech.qiusheng.entity.tools.ChartFileHandler;
import org.pockettech.qiusheng.entity.tools.SongFilterHandle;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

//这里是返回歌曲列表的服务,以及推荐列表的服务
@RestController
@Slf4j
public class StoreListServiceImpl implements StoreListService {
    @Resource
    SongDao songDao;
    @Resource
    ChartDao chartDao;

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


    @PostMapping("/admin/store/deleteSong")
    @ResponseBody
    public int deleteSong(@RequestParam int sid) throws FileNotFoundException {
        File songFolder = new File(ChartFileHandler.getLocalFilePath() + "_song_" + String.valueOf(sid));

        for (File f : Objects.requireNonNull(songFolder.listFiles()))
            if (!f.delete())
                return 1;

        if (!songFolder.delete())
            return 1;

        //TODO:添加删除歌曲以下所有铺面文件的选项与逻辑
        List<Chart> charts =chartDao.findSomeChartBySid(sid, 0, 50);
        for (Chart chart: charts) {
            if (chartDao.deleteChart(chart.getCid()) == 0) {
                log.info("删除谱面(cid: " + chart.getCid() + ")时出错，可能原因为数据库中并未存在该谱面");
                return 1;
            } else
                log.info("成功删除谱面(cid: " + chart.getCid() + ")");
        }

        int deleteCode = songDao.deleteSong(sid);

        if (deleteCode == 0) {
            log.info("删除歌曲(sid: " + sid + ")时出错，可能原因为数据库中并未存在该歌曲");
            return 1;
        } else
            log.info("成功删除歌曲(sid: " + sid + ")以及其下所有谱面文件和信息");

        return 0;
    }
}
