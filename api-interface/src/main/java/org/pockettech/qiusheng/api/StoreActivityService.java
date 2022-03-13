package org.pockettech.qiusheng.api;

import org.pockettech.qiusheng.entity.data.Event;
import org.pockettech.qiusheng.entity.result.ChartResult;
import org.pockettech.qiusheng.entity.result.ListResult;

public interface StoreActivityService {
    ListResult<Event> storeEvents(Integer active, Integer from);
    ListResult<ChartResult> eventCharts(Integer eid, Integer org, Integer from);
}
