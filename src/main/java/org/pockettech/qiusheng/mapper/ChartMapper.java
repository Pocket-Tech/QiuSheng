package org.pockettech.qiusheng.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.pockettech.qiusheng.entity.Chart;
import org.pockettech.qiusheng.entity.DTO.response.ChartResult;

import java.util.List;

@Mapper
public interface ChartMapper {

    public List<ChartResult> selectEventChartsByCids(@Param("cids") List<Integer> cids);

    public List<ChartResult> selectChart(Chart chart);

    public Chart selectChartById(@Param("cid") Integer cid);

    public void updateChart(Chart chart);
}
