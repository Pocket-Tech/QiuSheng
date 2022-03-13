package org.pockettech.qiusheng.dao;

import org.apache.ibatis.annotations.Param;
import org.pockettech.qiusheng.entity.data.Event;

import java.util.List;

public interface EventDao {
    List<Event> findEventsByActive(@Param("active") int active);
    String findCidListByEid(@Param("eid") int eid);
}
