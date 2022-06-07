package org.pockettech.qiusheng.dao;

import org.apache.ibatis.annotations.Param;
import org.pockettech.qiusheng.entity.data.Chart;

import java.util.List;

public interface ChartDao {
    int findChartCount();
    List<Chart> findSomeChartBySid(@Param("sid") int sid, @Param("first") int first, @Param("number") int number);
    List<Chart> findChartsBySid(@Param("sid") int sid);
    Chart findChartByCid(@Param("cid") int cid);
    Integer findSidByCid(@Param("cid") int cid);
    void uploadChartMsg(Chart chart);
    void updateChart(Chart chart);
    int deleteChart(@Param("cid") int cid);
}
