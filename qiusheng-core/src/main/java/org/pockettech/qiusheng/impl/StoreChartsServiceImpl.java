package org.pockettech.qiusheng.impl;

import lombok.AllArgsConstructor;
import lombok.Data;;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.pockettech.qiusheng.dao.UserDao;
import org.pockettech.qiusheng.entity.data.Chart;
import org.pockettech.qiusheng.entity.data.Song;
import org.pockettech.qiusheng.entity.data.User;
import org.pockettech.qiusheng.entity.charttransfer.ChartSign;
import org.pockettech.qiusheng.entity.charttransfer.Download;
import org.pockettech.qiusheng.api.ChartTransferService;
import org.pockettech.qiusheng.dao.ChartDao;
import org.pockettech.qiusheng.dao.SongDao;
import org.pockettech.qiusheng.entity.result.ChartResult;
import org.pockettech.qiusheng.entity.result.DownloadResult;
import org.pockettech.qiusheng.entity.result.ListResult;
import org.pockettech.qiusheng.entity.result.SongResult;
import org.pockettech.qiusheng.entity.builder.ChartFilterBuilder;
import org.pockettech.qiusheng.entity.charttransfer.MetaMsg;
import org.pockettech.qiusheng.entity.filter.ChartFilter;
import org.pockettech.qiusheng.entity.tools.ChartFileHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.DigestUtils;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

//这里是管理谱面仓库的服务
@Slf4j
@RestController
public class StoreChartsServiceImpl implements ChartTransferService {
    @Resource
    SongDao songDao;
    @Resource
    ChartDao chartDao;
    @Resource
    UserDao userDao;

    @Value("${server.port}")
    private String port;

    @Value("${host.localhost}")
    private String localhost;

    ChartFilterBuilder builder = new ChartFilterBuilder();

    public static HashMap<String, Integer> hashMap = new HashMap<>();
    public static HashMap<Integer, String> hashSongs = new HashMap<>();
    public static HashMap<Integer, String> hashImgs = new HashMap<>();
    //谱面列表
    @GetMapping("/api/store/charts")
    @ResponseBody
    public ListResult<ChartResult> storeCharts(@RequestParam(required = false) Integer sid,
                                               @RequestParam(required = false) Integer beta,
                                               @RequestParam(required = false) Integer mode,
                                               @RequestParam(required = false) Integer from,
                                               @RequestParam(required = false) Integer promote) {
        ChartFilter filter = builder.constructSimpleFilter(sid, beta, mode, from, promote);

        log.info("调用'/api/store/charts',正在查询歌曲下的谱面...");

        from = from == null ? 0 : from;
        int count = chartDao.findChartCount();
        boolean hasmore = (from + 50) < count;
        List<Chart> charts =chartDao.findSomeChartBySid(filter.getSid(), from, 50);
        List<ChartResult> result = new ArrayList<>();
        for (Chart chart:charts){
            ChartResult chartResult = new ChartResult(chart,chart.getSong(),chart.getUser().getUser_name());
            result.add(chartResult);
        }

        log.info(sid + "歌曲下的谱面查询成功");
        return new ListResult<>(1, hasmore, from + 50, result);
    }

    //谱面查询
    @GetMapping("/api/store/query")
    @ResponseBody
    public ListResult<SongResult> storeQuery(@RequestParam(required = false) Integer sid,
                                             @RequestParam(required = false) Integer cid,
                                             @RequestParam(required = false) Integer org) {
        ChartFilter filter = builder.constructQueryFilter(sid, cid, org);

        log.info("调用‘/api/store/query’，正在查询谱面...");

        List<SongResult> result = new ArrayList<>();
        Integer id = cid == null ? sid : chartDao.findSidByCid(cid);
        if (id != null) {
            Song song = songDao.findSongById(id);
            if (song != null) {
                SongResult songResult = new SongResult(song);
                result.add(songResult);
            } else {
                Chart chart = chartDao.findChartByCid(id);
                if (chart != null) {
                    song = songDao.findSongById(chart.getSid());
                    SongResult songResult = new SongResult(song);
                    result.add(songResult);
                }
            }
        }

        log.info("查询谱面成功");

        return new ListResult<>(1, false, 0, result);
    }

    //谱面上传 -> 获取签名
    @Override
    @PostMapping("/api/store/upload/sign")
    @ResponseBody
    public ChartSign uploadSign(@RequestParam(required = false) Integer sid,
                                @RequestParam(required = false) Integer cid,
                                @RequestParam(required = false) String name,
                                @RequestParam(required = false) String hash) {

        log.info("已收到上传" + name + "文件请求...");

        List<MetaMsg> msgs = new ArrayList<>();
        String[] names = name.split(",");
        String[] hashCodes = hash.split(",");

        for (int i = 0; i < names.length; i++){
            msgs.add(new MetaMsg(sid, cid, names[i], hashCodes[i]));
            hashMap.put(hashCodes[i],0);
        }
        hashImgs.put(sid,hashCodes[1]);
        hashSongs.put(sid,hashCodes[2]);

        log.info("第一阶段完成");
        ChartSign sign = new ChartSign(0, -1, "No Error", "http://" + localhost + ":" + port + "/api/store/uploading", msgs);
        return sign;
    }

    @PostMapping("/api/store/uploading")
    @ResponseBody
    //大致流程
    // 游戏客户端分别传输谱面（.mc）、图片（.jpg/png）、音频（.ogg）
    // 当收到谱面时，先更名后解压，再读取某些元数据，再删除原文件，将读到的元数据存入数据库
    // 当收到音频时，解析出音频时长存入数据库
    public void uploading(@RequestParam(required = false) MultipartFile file,
                            @RequestParam(required = false) Integer sid,
                            @RequestParam(required = false) Integer cid,
                            @RequestParam(required = false) String name,
                            @RequestParam(required = false) String hash) throws IOException {

        log.info("已收到" + name);

        try {
            String os = System.getProperty("os.name");
            String filePath = "";

            if (os.toLowerCase().startsWith("win")) {
                File path = new File(ResourceUtils.getURL("classpath:").getPath());
                filePath = path.getParentFile().getParentFile().getParent() + File.separator + "MalodyV" + File.separator;
                // 项目作为jar包运行时路径会带上“file:\\”，在此找到并删除
                int sub = filePath.indexOf("file:" + File.separator);
                if (sub != -1){
                    filePath = filePath.substring(sub + ("file:" + File.separator).length());
                }
            } else {
                filePath = "MalodyV" + File.separator;
            }

            filePath = filePath + "_song_" + sid + File.separator;
            log.info("----------上传路径为" + filePath + "----------");
            File tmp = new File(filePath);
            if(!tmp.exists()){
                tmp.mkdirs();
            }

            filePath = filePath + name;
            File file1 = new File(filePath);

            if (file1.exists()){
                FileInputStream fileInputStream = new FileInputStream(filePath);
                if (DigestUtils.md5DigestAsHex(fileInputStream).equals(hash)){
                    log.info("正在替换文件...");
                    fileInputStream.close();
                    file1.delete();
                }
            }

            //项目作为jar包运行时MultipartFile直接存储时tomcat会先存入临时文件，在此更换文件存入方式
            File targetFile = new File(filePath);
            FileUtils.writeByteArrayToFile(targetFile,file.getBytes());

            log.info("文件" + name + "存入成功");

            //TODO:此处存在可能的隐患：通常情况下客户端会优先传输铺面文件进行谱面信息在数据库登记，
            // 此后对音频文件进行分析即可更新数据库中的信息，不知道会不会存在不寻常的情况如谱面文件上传失败。
            if (ChartFileHandler.getFileExtension(name).equals(".mc")) {
                ChartFileHandler chartFileHandler = new ChartFileHandler("http://" + localhost + ":" + port + "/resource");
                chartFileHandler.setZipChartFile(filePath);
                log.info("----------文件路径为：" + filePath + "----------");
                log.info("初始化解压参数成功");
                chartFileHandler.unzipFile();
                log.info("文件" + name + "解压成功");
                chartFileHandler.getMetaFromFile();
                log.info("谱面文件" + name + "解析成功");

                File del = new File(filePath + "x");
                if (del.delete()){
                    log.info(name + "x" + "删除成功");
                } else {
                    log.info(name + "x" + "删除失败");
                }

                Chart chart = chartFileHandler.returnChart();
                Song song = chartFileHandler.returnSong();
                User user = chart.getUser();

                chart.setC_md5(hash);
                chart.setSid(sid);
                chart.setCid(cid);

                song.setSid(sid);
                song.setS_md5(hashSongs.get(sid));
                song.setImg_md5(hashImgs.get(sid));

                hashImgs.remove(sid);
                hashSongs.remove(sid);

                Song s = songDao.findSongById(sid);
                Chart c = chartDao.findChartByCid(cid);
                User u = userDao.findUserByName(user.getUser_name());

                log.info("谱面及歌曲信息正在写入数据库...");

                if (s == null && c == null){
                    song.setS_mode(1 << chart.getC_mode());
                    songDao.uploadSongMsg(song);

                    if (u == null)
                        userDao.uploadUser(user);

                    chart.setUid(user.getUid());
                    chartDao.uploadChartMsg(chart);
                    log.info("谱面及歌曲信息初始化成功");
                } else if (c == null){
                    int newMode = s.getS_mode();
                    newMode = newMode | (1 << chart.getC_mode());
                    song.setS_mode(newMode);

                    if (u == null) {
                        userDao.uploadUser(user);
                        chart.setUid(user.getUid());
                    } else {
                        chart.setUid(u.getUid());
                    }

                    songDao.updateSong(song);
                    chartDao.uploadChartMsg(chart);

                    log.info("谱面及歌曲mode信息更新成功");
                }
            } else if (ChartFileHandler.getFileExtension(name).equals(".ogg")) {
                int length = (int) ChartFileHandler.getOggLength(filePath);
                log.info("音频文件" + name + "解析成功，正在写入数据库...");

                songDao.updateLengthBySid(sid, length);

                log.info("数据写入成功,第二阶段阶段完成");
            }
            //到此表示文件写入成功，hashmap值改变
            hashMap.put(hash,1);
        } catch (IOException | CannotReadException e)   {
            e.printStackTrace();
            //到此表示文件写入失败
        }
    }

    //谱面上传 -> 完成上传
    @PostMapping("/api/store/upload/finish")
    @ResponseBody
    public ReturnCode finishUpload(@RequestParam(required = false) Integer sid,
                                   @RequestParam(required = false) Integer cid,
                                   @RequestParam(required = false) String name,
                                   @RequestParam(required = false) String hash,
                                   @RequestParam(required = false) Integer size,
                                   @RequestParam(required = false) String main) {
        log.info("收到确认请求，正在确认...");

        int result = -2;
        String[] names = name.split(",");
        String[] hashCodes = hash.split(",");

        if (names.length != hashCodes.length) {
            return new ReturnCode(-1);
        }
        //判断三个文件是否都成功通过第二阶段
        if (hashMap.get(hashCodes[0]) + hashMap.get(hashCodes[1]) + hashMap.get(hashCodes[2]) == 3 )
            result = 0;
        for (String str:hashCodes){
            hashMap.remove(str);
        }

        if (result == 0)
            log.info("确认无误，第三阶段完成");
        else
            log.info("好像出问题了...");
        return new ReturnCode(result);
    }

    //谱面下载
    @Override
    @GetMapping("/api/store/download")
    @ResponseBody
    public DownloadResult chartDownload(@RequestParam(required = false) int cid) {
        log.info("downloading...");

        Chart chart = chartDao.findChartByCid(cid);
        Song song = songDao.findSongById(chart.getSid());
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

    @PostMapping("/admin/store/deleteChart")
    @ResponseBody
    public int deleteChart(@RequestParam int cid) {
        int deleteCode = chartDao.deleteChart(cid);
        //TODO:添加删除铺面文件的选项与逻辑

        if (deleteCode == 0) {
            log.info("删除谱面(cid: " + cid + ")时出错，可能原因为数据库中并未存在该谱面");
            return 1;
        } else
            log.info("成功删除谱面(cid: " + cid + ")");

        return 0;
    }

    //返回代码类
    @Data
    @AllArgsConstructor
    static class ReturnCode {
        private int code;
    }
}
