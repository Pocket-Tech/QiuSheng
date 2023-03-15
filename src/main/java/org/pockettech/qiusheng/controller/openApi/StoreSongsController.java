package org.pockettech.qiusheng.controller.openApi;

import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.pockettech.qiusheng.constant.SystemConfig;
import org.pockettech.qiusheng.entity.DTO.response.ListResult;
import org.pockettech.qiusheng.entity.DTO.response.SongResult;
import org.pockettech.qiusheng.service.StoreSongService;
import org.pockettech.qiusheng.utils.PageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/store")
public class StoreSongsController {

    @Autowired
    private StoreSongService songService;

    // 歌曲列表
    @GetMapping("/list")
    public ListResult<Object> returnList(@RequestParam(required = false) String word,
                                             @RequestParam(required = false) Integer org,
                                             @RequestParam(required = false) Integer mode,
                                             @RequestParam(required = false) Integer lvge,
                                             @RequestParam(required = false) Integer lvle,
                                             @RequestParam(required = false) Integer beta,
                                             @RequestParam(required = false) Integer from) {
        log.info("‘/api/store/list’调用,正在查询歌曲列表...");

        // org不为0时，将分页页码重置为1
        from = (org != null && org != 0) ? 1 : (from == null ? 1 : from);

        PageUtils.startPage();
        List<SongResult> results = songService.selectSongList(mode, lvge, lvle, null);

        // 总数
        long total = new PageInfo<>(results).getTotal();

        log.info("已完成歌曲列表查询");

        return ListResult.success(((long) from++ * SystemConfig.PAGE_NUM) < total, from, Collections.singletonList(results));
    }

    //  推荐列表
    @GetMapping("/promote")
    public ListResult<Object> storePromote(@RequestParam(required = false) Integer org,
                                               @RequestParam(required = false) Integer mode,
                                               @RequestParam(required = false) Integer from){

        log.info("‘/api/store/promote’调用,正在查询推荐列表...");

        // org不为0时，将分页页码重置为1
        from = (org != null && org != 0) ? 1 : (from == null ? 1 : from);

        PageUtils.startPage();
        List<SongResult> results = songService.selectSongList(mode, null, null, 1);

        // 总数
        long total = new PageInfo<>(results).getTotal();

        log.info("已完成推荐列表查询");

        return ListResult.success(((long) from++ * SystemConfig.PAGE_NUM) < total, from, Collections.singletonList(results));
    }
}
