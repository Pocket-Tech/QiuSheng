package org.pockettech.qiusheng;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.TypeReference;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.pockettech.qiusheng.cache.CacheData;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@EnableScheduling
@SpringBootApplication
@MapperScan(value = "org.pockettech.qiusheng.mapper")
public class QiuShengApplication implements CommandLineRunner {

    public static void main(String[] args) {
        try {
            SpringApplication.run(QiuShengApplication.class, args);
        } catch (Exception e){
            log.error("报错原因 ============== ", e);
        }
    }

    /**
     * 程序启动成功执行
     */
    @Override
    public void run(String... arg){
        log.info("谱面服务器已启动.");
    }

    /**
     * 每天凌晨4点执行，清除系统业务产生的缓存
     */
    @Scheduled(cron="0 0 4 * * ? ")
//    @Scheduled(cron="*/10 * * * * ? ") 10s一次
    public void cleanCache() {

        log.info("开始清理系统缓存...");

        try {
            // 获取CacheData类
            Class<?> CacheDataClass = Class.forName("org.pockettech.qiusheng.cache.CacheData");

            // 获取CacheData类的属性
            Field[] fields = CacheDataClass.getFields();

            for(Field field : fields ) {
                // 清理令牌缓存
                if (Arrays.asList(CacheData.TIME_FIELD).contains(field.getName())) {
                    try {
                        // 不需要清理的令牌缓存暂存
                        Map<String, Object> newCache = new HashMap<>();

                        // 获取属性值
                        Object obj = field.get(CacheDataClass.getDeclaredConstructor().newInstance());
                        JSONObject tokenCache = JSONObject.parseObject(JSON.toJSONString(obj));

                        // 遍历键值
                        for (Object o : tokenCache.keySet()) {

                            // 属性值
                            Map<String, Object> value = JSONObject.parseObject(tokenCache.get(o.toString()).toString(), new TypeReference<Map<String, Object>>(){});

                            // 无过期时间默认清理
                            if (value.get("timeOut") == null) {
                                continue;
                            }

                            // 获取过期时间
                            long timeOut = Long.parseLong(value.get("timeOut").toString());

                            // 当前时间小于于过期时间，不清理
                            if (new Date().getTime() < timeOut) {
                                newCache.put(o.toString(), value);
                            }
                        }

                        // 更新令牌
                        field.set(null, newCache);
                    } catch (Exception e) {
                        e.printStackTrace();
                        log.error("按时间清理令牌缓存失败，正在尝试全部清理...");
                        field.set(null, new HashMap<>());
                    }
                } else {
                    field.set(null, new HashMap<>());
                    log.info("已清空" + field.getName() + "属性的值");
                }
            }

        } catch (Exception e) {
            log.error("清理失败，错误信息：" + e.getMessage());
        }

        log.info("清理完成");
    }
}
