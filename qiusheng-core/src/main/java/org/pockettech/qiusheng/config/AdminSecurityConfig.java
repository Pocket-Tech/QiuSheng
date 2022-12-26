package org.pockettech.qiusheng.config;

import org.pockettech.qiusheng.fliter.JwtAuthenticationTokenFilter;
import org.pockettech.qiusheng.handler.LoginFailureHandler;
import org.pockettech.qiusheng.impl.admin.AdminDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
//@EnableWebSecurity
public class AdminSecurityConfig extends WebSecurityConfigurerAdapter {


    @Autowired
    private JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter;

    @Autowired
    private AuthenticationEntryPoint authenticationEntryPoint;

    @Autowired
    private AccessDeniedHandler accessDeniedHandler;

    // security认证白名单
    private static final String[] AUTH_WHITELIST = {
            "/",
            "/api/**",
            "/resource/**"
    };

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()//TODO:尝试在qiusheng-terminal中加入csrf防护
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .formLogin()
                .loginProcessingUrl("/admin/login")
                .defaultSuccessUrl("/admin/info")
                .failureHandler(new LoginFailureHandler())
                .permitAll()
                .and()
                .authorizeHttpRequests()
                // 无需登录认证的接口
                .antMatchers(AUTH_WHITELIST).permitAll()
                // 任何请求
                .anyRequest().authenticated();

        // token验证过滤器
        http.addFilterBefore(jwtAuthenticationTokenFilter, UsernamePasswordAuthenticationFilter.class);

        // 异常处理
        http.exceptionHandling()
                .authenticationEntryPoint(authenticationEntryPoint)
                .accessDeniedHandler(accessDeniedHandler);

        // 跨域
        http.cors();
    }
}
