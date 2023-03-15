package org.pockettech.qiusheng.service;

import org.apache.ibatis.annotations.Param;
import org.pockettech.qiusheng.entity.Event;

import java.util.List;

public interface StoreEventsService {

    public List<Event> getstoreEventList(Integer active);

    public String findCidListByEid(int eid);

    public void addEvent(Event event);

    public void updateEvent(Event event);

    public int deleteEvent(int eid);

}
