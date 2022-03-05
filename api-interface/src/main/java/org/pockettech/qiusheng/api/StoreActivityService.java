package org.pockettech.qiusheng.api;

import org.pockettech.qiusheng.entity.result.ActivityResult;
import org.pockettech.qiusheng.entity.result.ChartResult;
import org.pockettech.qiusheng.entity.result.ListResult;

public interface StoreActivityService {
    ListResult<ActivityResult> storeEvents(Integer active, Integer from);
    ListResult<ChartResult> eventCharts(Integer eid, Integer org, Integer from);
}
