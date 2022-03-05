package org.pockettech.qiusheng.api;

import org.pockettech.qiusheng.entity.data.Song;

import java.util.List;

public interface CenterListService {
    List<Song> getListFromHosts();
}
