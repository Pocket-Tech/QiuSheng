package org.pockettech.qiusheng.controller.openApi;

import com.alibaba.fastjson2.JSONObject;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.pockettech.qiusheng.cache.CacheData;
import org.pockettech.qiusheng.constant.ResultCode;
import org.pockettech.qiusheng.constant.SystemConfig;
import org.pockettech.qiusheng.entity.Chart;
import org.pockettech.qiusheng.entity.DTO.response.*;
import org.pockettech.qiusheng.entity.Download;
import org.pockettech.qiusheng.entity.Song;
import org.pockettech.qiusheng.entity.User;
import org.pockettech.qiusheng.service.StoreChartService;
import org.pockettech.qiusheng.service.StoreSongService;
import org.pockettech.qiusheng.service.UserService;
import org.pockettech.qiusheng.utils.PageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Slf4j
@Controller
@RequestMapping("/api/store")
public class StoreChartsController {

    @Autowired
    private StoreChartService chartService;

    @Autowired
    private StoreSongService songService;

    @Autowired
    private UserService userService;

    //谱面列表
    @GetMapping("/charts")
    @ResponseBody
    public ListResult<Object> storeCharts(Chart chart, @RequestParam(required = false) Integer from) {

        log.info("调用'/api/store/charts',正在查询歌曲下的谱面...");

        from = from == null ? 0 : from;

        PageUtils.startPage(null, from);

        // 符合条件的谱面列表
        List<ChartResult> results = chartService.selectChart(chart);
        // 总数
        long total = new PageInfo<>(results).getTotal();

        log.info(chart.getSid() + "歌曲下的谱面查询成功");

        return ListResult.success(((long) from++ * SystemConfig.PAGE_NUM) < total, from, Collections.singletonList(results));
    }

    //谱面查询
    @GetMapping("/query")
    @ResponseBody
    public ListResult<Object> storeQuery(@RequestParam(required = false) Integer sid,
                                         @RequestParam(required = false) Integer cid,
                                         @RequestParam(required = false) Integer org) {

        log.info("调用‘/api/store/query’，正在查询谱面...");


        // 符合条件的歌曲列表
        List<SongResult> result = songService.selectSongResultList(sid, cid);

        log.info("查询谱面成功");

        return ListResult.success(Collections.singletonList(result));
    }

    //谱面上传 -> 获取签名
    @PostMapping("/upload/sign")
    @ResponseBody
    public ChartSign uploadSign(@RequestParam(required = false) Integer sid,
                                @RequestParam(required = false) Integer cid,
                                @RequestParam(required = false) String name,
                                @RequestParam(required = false) String hash) {

        log.info("已收到上传" + name + "文件请求...");

        // 检查cid, sid是否已存在


        List<JSONObject> objects = chartService.getSign(sid, cid, name, hash);

        log.info("第一阶段完成");
        return new ChartSign(0, -1, "No Error", "http://" + SystemConfig.HOST + ":" + SystemConfig.PORT + "/api/store/uploading", objects);
    }

    @PostMapping("/uploading")
    @ResponseBody
    //大致流程
    // 游戏客户端分别传输谱面（.mc）、图片（.jpg/png）、音频（.ogg）
    // 当收到谱面时，先更名后解压，再读取某些元数据，再删除原文件，将读到的元数据存入数据库
    // 当收到音频时，解析出音频时长存入数据库
    public CommonResult<Object> uploading(@RequestParam MultipartFile file, @RequestParam Integer sid,
                          @RequestParam Integer cid, @RequestParam String name,
                          @RequestParam String hash, @RequestParam String sign) {
        log.info("已收到" + name);

        if(!CacheData.signMap.get(cid).equals(sign)) {
            return CommonResult.error(ResultCode.SIGN_VERIFY_FAILED);
        }

        try {
            String filePath = chartService.saveFile(file, sid, name, hash);

            if (!chartService.fileHandler(sid, name, hash, filePath)) {
                return CommonResult.error(ResultCode.FILE_PROCESSING_FAILED);
            }

            return CommonResult.success();
        } catch (IOException e) {
            //到此表示文件写入失败
            return CommonResult.error(ResultCode.FILE_PROCESSING_FAILED);
        }
    }

    //谱面上传 -> 完成上传
    @PostMapping("/api/store/upload/finish")
    @ResponseBody
    public CommonResult<Object> finishUpload(@RequestParam(required = false) Integer sid,
                                   @RequestParam(required = false) Integer cid,
                                   @RequestParam(required = false) String name,
                                   @RequestParam(required = false) String hash,
                                   @RequestParam(required = false) Integer size,
                                   @RequestParam(required = false) String main) {
        log.info("收到确认请求，正在确认...");

        int result = chartService.confirmFile(name, hash);

        if (result == 0) {
            log.info("确认无误，第三阶段完成，正在存入本地数据库...");
            Chart chart = CacheData.hashChart.get(sid);
            User user = chart.getUser();
            userService.updateUser(user);

            chart.setUid(user.getUid());
            songService.updateSong(chart.getSong());
            chartService.updateChart(chart);
            CacheData.hashChart.remove(sid);

            return CommonResult.success();
        }
        else {
            log.info("好像出问题了...");
            return CommonResult.error(ResultCode.FILE_MISSING);
        }
    }

    //谱面下载
    @GetMapping("/download")
    @ResponseBody
    public DownloadResult chartDownload(@RequestParam(required = false) int cid) {
        log.info("downloading...");

        Chart chart = chartService.selectChartById(cid);
        Song song = songService.selectSongById(chart.getSid());

        String mcFilePath = chart.getC_file_path();
        String coverPath = song.getImg_file_path();
        String oggPath = song.getS_file_path();

        List<Download> items = new ArrayList<>();
        Download mc = new Download(mcFilePath.substring(mcFilePath.lastIndexOf("/") + 1), chart.getC_md5(), mcFilePath);
        items.add(mc);
        Download cover = new Download(coverPath.substring(coverPath.lastIndexOf("/") + 1), song.getImg_md5(), coverPath);
        items.add(cover);
        Download ogg = new Download(oggPath.substring(oggPath.lastIndexOf("/") + 1), song.getS_md5(), oggPath);
        items.add(ogg);

        return new DownloadResult(0, items, chart.getSid(), chart.getCid());
    }
}
