package org.pockettech.qiusheng.controller.openApi;

import lombok.extern.slf4j.Slf4j;
import org.pockettech.qiusheng.entity.DTO.response.ChartResult;
import org.pockettech.qiusheng.entity.DTO.response.ListResult;
import org.pockettech.qiusheng.entity.Event;
import org.pockettech.qiusheng.service.StoreChartService;
import org.pockettech.qiusheng.service.StoreEventsService;
import org.pockettech.qiusheng.utils.PageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Controller
@RequestMapping("/api/store")
public class StoreEventsController {

    @Autowired
    private StoreEventsService eventsService;

    @Autowired
    private StoreChartService chartService;

    /**
     * 获取所有活动列表
     * @param active 返回当前有效的活动
     * @param from 翻页起点
     * @return 活动列表
     */
    @GetMapping("/events")
    @ResponseBody
    public ListResult<Object> storeEvents(@RequestParam(required = false) Integer active,
                                            @RequestParam(required = false) Integer from){

        log.info("正在调用‘/api/store/events’查找活动列表...");

        from = from == null ? 0 : from;

        PageUtils.startPage(null, from);
        List<Event> result = eventsService.getstoreEventList(active);

        log.info("活动列表查找成功！");

        return ListResult.success(from + 1, Collections.singletonList(result));
    }

    /**
     * 获取指定活动下谱面
     * @param eid 活动id
     * @param org 是否返回原始标题
     * @param from 翻页起点
     * @return 活动下谱面
     */
    @GetMapping("/event")
    @ResponseBody
    public ListResult<Object> eventCharts(@RequestParam(required = false) Integer eid,
                                               @RequestParam(required = false) Integer org,
                                               @RequestParam(required = false) Integer from){

        log.info("调用‘/api/store/event’，正在查询活动谱面...");

        List<ChartResult> result = new ArrayList<>();

        // org不为0时，将分页页码重置为1
        from = (org != null && org != 0) ? 1 : (from == null ? 1 : from);

        String cids = eventsService.findCidListByEid(eid);

        if (cids != null && !"".equals(cids)) {
            List<Integer> cidList = Arrays.stream(cids.split(",")).mapToInt(Integer::parseInt).boxed().collect(Collectors.toList());
            PageUtils.startPage(null, from);
            result = chartService.selectEventCharts(cidList);
        }

        log.info("查询活动谱面成功！");

        return ListResult.success(from + 1, Collections.singletonList(result));
    }
}
