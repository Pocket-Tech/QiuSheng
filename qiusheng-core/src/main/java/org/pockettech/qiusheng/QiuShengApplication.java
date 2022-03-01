package org.pockettech.qiusheng;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.annotation.EnableTransactionManagement;

//启动类
@SpringBootApplication
@EnableAsync
@Slf4j
@MapperScan(value = "org.pockettech.qiusheng.dao")
@EnableTransactionManagement
public class QiuShengApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication(QiuShengApplication.class);
        springApplication.run(args);
    }

    @Override
    public void run(String... arg) throws InterruptedException {
        log.info("谱面服务器已启动.");
    }
}


