package org.pockettech.qiusheng.service.impl;

import com.alibaba.fastjson2.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.pockettech.qiusheng.cache.CacheData;
import org.pockettech.qiusheng.constant.SystemConfig;
import org.pockettech.qiusheng.entity.Chart;
import org.pockettech.qiusheng.entity.DTO.response.ChartResult;
import org.pockettech.qiusheng.entity.Song;
import org.pockettech.qiusheng.handler.ChartFileHandler;
import org.pockettech.qiusheng.mapper.ChartMapper;
import org.pockettech.qiusheng.service.StoreChartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class StoreChartServiceImpl implements StoreChartService {

    @Autowired
    private ChartMapper chartMapper;

    @Override
    public List<ChartResult> selectEventCharts(List<Integer> cids) {
        return chartMapper.selectEventChartsByCids(cids);
    }

    @Override
    public List<ChartResult> selectChart(Chart chart) {
        return chartMapper.selectChart(chart);
    }

    @Override
    public Chart selectChartById(Integer cid) {
        return chartMapper.selectChartById(cid);
    }

    @Override
    public void updateChart(Chart chart) {
        chartMapper.updateChart(chart);
    }

    @Override
    public List<JSONObject> getSign(Integer sid, Integer cid, String name, String hash) {

        String uuid = UUID.randomUUID().toString();

        List<JSONObject> objects = new ArrayList<>();
        JSONObject json = new JSONObject();

        String[] names = name.split(",");
        String[] hashCodes = hash.split(",");

        for (int i = 0; i < names.length; i++){
            json.put("sid", sid);
            json.put("cid", cid);
            json.put("name", names[i]);
            json.put("hash", hashCodes[i]);
            json.put("sign", uuid);

            objects.add(json);
            CacheData.hashMap.put(hashCodes[i],0);
        }

        CacheData.signMap.put(cid, uuid);
        CacheData.hashChart.put(sid,new Chart(hashCodes[0]));
        CacheData.hashChart.get(sid).setSong(new Song(hashCodes[2], hashCodes[1]));

        return objects;
    }

    @Override
    public boolean fileHandler(Integer sid, String name, String hash, String filePath) {
        //TIP:此处存在可能的隐患：通常情况下客户端会优先传输铺面文件进行谱面信息在数据库登记，
        // 此后对音频文件进行分析即可更新数据库中的信息，不知道会不会存在不寻常的情况如谱面文件上传失败。
        boolean result;

        if (ChartFileHandler.getFileExtension(name).equals(".mc")) {
            result = MCFileHandler(sid, name, hash, filePath);
        } else if (ChartFileHandler.getFileExtension(name).equals(".ogg")) {
            result = OGGFileHandler(sid, name, hash, filePath);
        } else {
            result = imageFileHandler(sid, hash);
        }
        //到此表示文件写入成功，hashmap值改变
        CacheData.hashMap.put(hash,1);
        return result;
    }

    public boolean MCFileHandler(Integer sid, String name, String hash, String filePath) {

        try {
            //判断此阶段的MD5与第一阶段MD5是否一致
            if (!CacheData.hashChart.get(sid).getC_md5().equals(hash)) {
                log.info("谱面md5不一致");
                return false;
            }

            ChartFileHandler chartFileHandler = new ChartFileHandler("http://" + SystemConfig.HOST + ":" + SystemConfig.PORT + "/resource");
            chartFileHandler.setZipChartFile(filePath);
            log.info("----------文件路径为：" + filePath + "----------");
            chartFileHandler.unzipFile();
            log.info("文件" + name + "解压成功");
            chartFileHandler.getMetaFromFile();
            log.info("谱面文件" + name + "解析成功");

            File del = new File(filePath + "x");
            if (del.delete()) {
                log.info(name + "x" + "删除成功");
            } else {
                log.info(name + "x" + "删除失败");
            }

            Chart chart = chartFileHandler.returnChart();
            Song song = chartFileHandler.returnSong();

            song.setS_md5(CacheData.hashChart.get(sid).getSong().getS_md5());
            chart.setC_md5(CacheData.hashChart.get(sid).getC_md5());
            song.setImg_md5(CacheData.hashChart.get(sid).getSong().getImg_md5());

            chart.setSong(song);

            CacheData.hashChart.put(sid, chart);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean OGGFileHandler(Integer sid, String name, String hash, String filePath) {
        try {
            if (!CacheData.hashChart.get(sid).getSong().getS_md5().equals(hash)) {
                log.info("歌曲md5不一致");
                return false;
            }

            int length = (int) ChartFileHandler.getOggLength(filePath);
            log.info("音频文件" + name + "解析成功...");

            CacheData.hashChart.get(sid).getSong().setLength(length);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean imageFileHandler(Integer sid, String hash) {
        if (!CacheData.hashChart.get(sid).getSong().getImg_md5().equals(hash)) {
            log.info("图片md5不一致");
            return false;
        }
        return true;
    }

    @Override
    public String saveFile(MultipartFile file, Integer sid, String name, String hash) throws IOException {
        String filePath = ChartFileHandler.getLocalFilePath();

        filePath = filePath + "_song_" + sid + File.separator;
        log.info("----------上传路径为" + filePath + "----------");
        File tmp = new File(filePath);
        if (!tmp.exists() && tmp.mkdirs()) {
            log.info("新建文件夹：" + tmp);
        }

        filePath = filePath + name;
        File file1 = new File(filePath);

        if (file1.exists()){
            FileInputStream fileInputStream = new FileInputStream(filePath);
            if (DigestUtils.md5DigestAsHex(fileInputStream).equals(hash)){
                log.info("正在替换文件...");
                fileInputStream.close();
                if (file1.delete()) {
                    log.info("文件:" + file1 + "删除成功");
                }
            }
        }

        //项目作为jar包运行时MultipartFile直接存储时tomcat会先存入临时文件，在此更换文件存入方式
        File targetFile = new File(filePath);
        FileUtils.writeByteArrayToFile(targetFile,file.getBytes());

        log.info("文件" + name + "存入成功");
        return filePath;
    }

    @Override
    public int confirmFile(String name, String hash) {
        int result = -2;
        String[] names = name.split(",");
        String[] hashCodes = hash.split(",");

        if (names.length != hashCodes.length) {
            return result;
        }
        //判断三个文件是否都成功通过第二阶段
        if (CacheData.hashMap.get(hashCodes[0]) + CacheData.hashMap.get(hashCodes[1]) + CacheData.hashMap.get(hashCodes[2]) == 3 )
            result = 0;
        for (String str:hashCodes){
            CacheData.hashMap.remove(str);
        }

        return result;
    }
}
