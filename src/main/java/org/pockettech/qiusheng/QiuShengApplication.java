package org.pockettech.qiusheng;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.pockettech.qiusheng.cache.CacheData;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.lang.reflect.Field;
import java.util.HashMap;

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

    @Override
    public void run(String... arg){
        log.info("谱面服务器已启动.");
    }

    /**
     * 每天凌晨4点执行，清除系统业务产生的缓存
     */
    @Scheduled(cron="0 0 4 * * ? ")
    public void cleanCache() {

        log.info("开始清理系统缓存...");

        try {
            Class<?> CacheDataClass = Class.forName("org.pockettech.qiusheng.cache.CacheData");
            Field[] fields = CacheDataClass.getFields();

            for(Field field : fields ){
                field.set(null, new HashMap<>());
                log.info("已清空" + field.getName() + "属性的值");
            }

        } catch (Exception e) {
            log.error("清理失败，错误信息：" + e.getMessage());
        }

        log.info("清理完成");
    }
}
