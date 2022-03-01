package org.pockettech.qiusheng.impl;

import lombok.extern.slf4j.Slf4j;
import org.pockettech.qiusheng.api.StoreActivityService;
import org.pockettech.qiusheng.entity.Result.ActivityResult;
import org.pockettech.qiusheng.entity.Result.ChartResult;
import org.pockettech.qiusheng.entity.Result.ListResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

//活动分区
@Slf4j
@RestController
public class StoreEventsServiceImpl implements StoreActivityService {
//    获取所有活动列表
    //计划用url接收active与from不使用实体
    @Override
    @GetMapping("/api/store/events")
    @ResponseBody
    public ListResult<ActivityResult> storeEvents(@RequestParam(required = false) Integer active,
                                                  @RequestParam(required = false) Integer from){

        log.info("正在调用‘/api/store/events’查找活动列表...");

        List<ActivityResult> result = new ArrayList<>();
        ActivityResult activity = new ActivityResult(1,"name","sponsor","start","end",true,"cover");
        result.add(activity);

        log.info("活动列表查找成功！");

        return new ListResult<>(1,true,2,result);
    }

//    获取指定活动下所有谱面
//    计划用url接收eid,org,from不使用实体
    @Override
    @GetMapping("/api/store/event")
    @ResponseBody
    public ListResult<ChartResult> eventCharts(@RequestParam(required = false) Integer eid,
                                                  @RequestParam(required = false) Integer org,
                                                  @RequestParam(required = false) Integer from){

        log.info("调用‘/api/store/event’，正在查询活动谱面...");

        List<ChartResult> result = new ArrayList<>();
        ChartResult chartResult = new ChartResult(1,1,1,"creator","title","artist","version",1,1,1,"cover",1,1,"file_path");
        result.add(chartResult);

        log.info("查询活动谱面成功！");

        return new ListResult<>(1,true,2,result);
    }
}
