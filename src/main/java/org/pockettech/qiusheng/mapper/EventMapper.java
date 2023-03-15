package org.pockettech.qiusheng.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.pockettech.qiusheng.entity.Event;

import java.util.List;

@Mapper
public interface EventMapper {
    List<Event> findEventsByActive(@Param("active") int active);

    String findCidListByEid(@Param("eid") int eid);

    void addEvent(Event event);

    void updateEvent(Event event);

    int deleteEvent(@Param("eid") int eid);
}
