package org.pockettech.qiusheng.impl;

import lombok.extern.slf4j.Slf4j;
import org.pockettech.qiusheng.api.StoreActivityService;
import org.pockettech.qiusheng.dao.ChartDao;
import org.pockettech.qiusheng.dao.EventDao;
import org.pockettech.qiusheng.dao.SongDao;
import org.pockettech.qiusheng.dao.UserDao;
import org.pockettech.qiusheng.entity.data.Chart;
import org.pockettech.qiusheng.entity.data.Event;
import org.pockettech.qiusheng.entity.data.Song;
import org.pockettech.qiusheng.entity.data.User;
import org.pockettech.qiusheng.entity.result.ChartResult;
import org.pockettech.qiusheng.entity.result.ListResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

//活动分区
@Slf4j
@RestController
//@Transactional(propagation = Propagation.REQUIRED,isolation = Isolation.DEFAULT,timeout=36000,rollbackFor=Exception.class)
public class StoreEventsServiceImpl implements StoreActivityService {

    @Resource
    EventDao eventDao;

    @Resource
    ChartDao chartDao;

    @Resource
    UserDao userDao;

    @Resource
    SongDao songDao;

//    获取所有活动列表
    //计划用url接收active与from不使用实体
    @Override
    @GetMapping("/api/store/events")
    @ResponseBody
    public ListResult<Event> storeEvents(@RequestParam(required = false) Integer active,
                                         @RequestParam(required = false) Integer from){

        log.info("正在调用‘/api/store/events’查找活动列表...");
        System.out.println(active);

        List<Event> result = eventDao.findEventsByActive(1);

        log.info("活动列表查找成功！");

        return new ListResult<>(0,true,2,result);
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

        String cids = eventDao.findCidListByEid(eid);
        String[] cid_list = cids.split(",");

        for (String cid : cid_list){
            Chart chart = chartDao.findChartByCid(Integer.parseInt(cid));
            User user = userDao.findNameByUid(chart.getUid());
            Song song = songDao.findSongById(chart.getSid());
            ChartResult chartResult = new ChartResult(chart,song,user.getUser_name());
            result.add(chartResult);
        }

        log.info("查询活动谱面成功！");

        return new ListResult<>(0,true,2,result);
    }
}
