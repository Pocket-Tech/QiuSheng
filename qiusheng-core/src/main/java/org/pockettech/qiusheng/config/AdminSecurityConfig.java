package org.pockettech.qiusheng.config;

import org.pockettech.qiusheng.impl.admin.AdminDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
public class AdminSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private AdminDetailService adminDetailService;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(adminDetailService).passwordEncoder(new BCryptPasswordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                    .antMatchers("/", "/api/**", "/resource/**")
                    .permitAll()
                .anyRequest()
                    .authenticated()
                .and()
                .formLogin()
                    .loginProcessingUrl("/admin/login")
                    .permitAll()
                .and()
                .csrf().disable();//TODO:尝试在qiusheng-terminal中加入csrf防护
    }
}
