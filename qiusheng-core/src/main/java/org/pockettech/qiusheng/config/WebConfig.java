package org.pockettech.qiusheng.config;

import org.pockettech.qiusheng.entity.tools.ChartFileHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.io.IOException;

@Configuration
@EnableWebMvc
public class WebConfig extends WebMvcConfigurerAdapter {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String file_path = null;

        try {
            file_path = ChartFileHandler.getLocalFilePath();
        } catch (IOException e) {
            e.printStackTrace();
        }

        registry.addResourceHandler("/resource/**")
                .addResourceLocations("file:" + file_path);//"file:" + file_path
    }
}
