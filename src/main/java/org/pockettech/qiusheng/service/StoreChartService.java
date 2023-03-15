package org.pockettech.qiusheng.service;

import com.alibaba.fastjson2.JSONObject;
import org.pockettech.qiusheng.entity.Chart;
import org.pockettech.qiusheng.entity.DTO.response.ChartResult;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

public interface StoreChartService {

    public List<ChartResult> selectEventCharts(List<Integer> cids);

    public List<ChartResult> selectChart(Chart chart);

    public Chart selectChartById(Integer cid);

    public void updateChart(Chart chart);

    public List<JSONObject> getSign(Integer sid, Integer cid, String name, String hash);

    public boolean fileHandler(Integer sid, String name, String hash, String filePath);

    public String saveFile(MultipartFile file, Integer sid, String name, String hash) throws IOException;

    public int confirmFile(String name, String hash);
}
