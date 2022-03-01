package org.pockettech.qiusheng.entity.tools;

import lombok.AllArgsConstructor;
import org.pockettech.qiusheng.entity.Data.Song;
import org.pockettech.qiusheng.entity.filter.StoreConditionalFilter;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
public class SongFilterHandle {
    StoreConditionalFilter filter;

    public List<Song> getSongsByMode(List<Song> songs) {
        List<Song> result = new ArrayList<>();
        for (Song song: songs) {
            if ((song.getS_mode() | (1 << filter.getMode())) == song.getS_mode())
                result.add(song);
        }
        return result;
    }
}
