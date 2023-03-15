package org.pockettech.qiusheng.service.impl;

import org.pockettech.qiusheng.entity.Event;
import org.pockettech.qiusheng.mapper.EventMapper;
import org.pockettech.qiusheng.service.StoreEventsService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class StoreEventsServiceImpl implements StoreEventsService {

    @Resource
    private EventMapper eventMapper;

    @Override
    public List<Event> getstoreEventList(Integer active) {
        return eventMapper.findEventsByActive(active);
    }

    @Override
    public String findCidListByEid(int eid) {
        return eventMapper.findCidListByEid(eid);
    }

    @Override
    public void addEvent(Event event) {
        eventMapper.addEvent(event);
    }

    @Override
    public void updateEvent(Event event) {
        eventMapper.updateEvent(event);
    }

    @Override
    public int deleteEvent(int eid) {
        return eventMapper.deleteEvent(eid);
    }
}
