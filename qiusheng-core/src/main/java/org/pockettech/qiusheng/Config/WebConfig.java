package org.pockettech.qiusheng.Config;

import org.springframework.context.annotation.Configuration;
import org.springframework.util.ResourceUtils;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.io.File;
import java.io.IOException;

@Configuration
@EnableWebMvc
public class WebConfig extends WebMvcConfigurerAdapter {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String file_path = null;
        try {
            File path = new File(ResourceUtils.getURL("classpath:").getPath());
            file_path = path.getParentFile().getParentFile().getParent() + File.separator + "MalodyV" + File.separator;
            int sub = file_path.indexOf("file:\\");
            if (sub != -1){
                file_path = file_path.substring(sub + "file:\\".length());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        registry.addResourceHandler("/resource/**")
                .addResourceLocations("file:" + file_path);//"file:" + file_path
    }
}
