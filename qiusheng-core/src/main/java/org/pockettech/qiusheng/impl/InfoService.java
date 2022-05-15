package org.pockettech.qiusheng.impl;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

//这里是返回服务器基本信息的服务
@Controller
public class InfoService {
    Information info = new Information();

    @Value("${qiusheng.greeting}")
    String greeting;

    @GetMapping("/api/store/info")
    @ResponseBody
    public Information returnInfo() {
        if (!greeting.equals(""))
            info.setWelcome(greeting);
        return info;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Information {
        private int code = 0;
        private int api = 202112;
        private int min = 202112;
        private String welcome = "Welcome to QiuSheng Server";
    }
}
