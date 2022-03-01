package org.pockettech.qiusheng.api;

import org.pockettech.qiusheng.entity.Result.ActivityResult;
import org.pockettech.qiusheng.entity.Result.ChartResult;
import org.pockettech.qiusheng.entity.Result.ListResult;

public interface StoreActivityService {
    ListResult<ActivityResult> storeEvents(Integer active, Integer from);
    ListResult<ChartResult> eventCharts(Integer eid, Integer org, Integer from);
}
