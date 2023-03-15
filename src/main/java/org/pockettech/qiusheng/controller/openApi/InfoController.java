package org.pockettech.qiusheng.controller.openApi;

import org.pockettech.qiusheng.constant.SystemConfig;
import org.pockettech.qiusheng.entity.Information;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/store")
public class InfoController {

    Information info = new Information();

    /**
     * 获取连接服务器欢迎语
     */
    @GetMapping("/info")
    public Information returnInfo() {
        if (SystemConfig.WELCOME != null && !SystemConfig.WELCOME.equals(""))
            info.setWelcome(SystemConfig.WELCOME);
        return info;
    }
}
