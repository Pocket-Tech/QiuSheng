package org.pockettech.qiusheng.constant;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 系统配置文件信息
 */
@Data
@Component
public class SystemConfig {

    // 服务器欢迎语
    public static String WELCOME;

    public static String ADMIN_WELCOME;


    // 单次登录状态维持时间（单位：小时）
    public static Long JWT_TTL;

    // jwt加密密钥
    public static String JWT_KEY;

    // 服务器域名或ip
    public static String HOST;

    // 使用端口
    public static Integer PORT;

    public static Integer PAGE_NUM;

    // 默认路径
    public static String ROOT_RESOURCE_PATH = "http://" + HOST + ":" + PORT + "/resource";

    @Value("${info.qiusheng.greeting}")
    public void setWelcome(String welcome) {
        SystemConfig.WELCOME = welcome;
    }

    @Value("${info.qiusheng.adminGreeting}")
    public void setAdminWelcome(String adminWelcome) {
        SystemConfig.ADMIN_WELCOME = adminWelcome;
    }

    @Value("${info.jwt.ttl}")
    public void setJwtTtl(Long jwtTtl) {
        SystemConfig.JWT_TTL = jwtTtl;
    }

    @Value("${info.jwt.key}")
    public void setJwtKey(String jwtKey) {
        SystemConfig.JWT_KEY = jwtKey;
    }

    @Value("${info.host}")
    public void setHost(String host) {
        SystemConfig.HOST = host;
    }

    @Value("${server.port}")
    public void setPort(Integer port) {
        SystemConfig.PORT = port;
    }

    @Value("${info.pageHelper.pageNum}")
    public void setPageNum(Integer pageNum) {
        SystemConfig.PAGE_NUM = pageNum;
    }
}
