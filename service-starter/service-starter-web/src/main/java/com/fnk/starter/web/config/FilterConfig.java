package com.fnk.starter.web.config;

import com.fnk.starter.web.filter.LogMdcFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;

/**
 * 过滤器配置
 * @author Enigma
 */
public class FilterConfig {
    @Bean
    public FilterRegistrationBean<LogMdcFilter> regLogMdcFilter() {
        FilterRegistrationBean<LogMdcFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(new LogMdcFilter());
        registration.addUrlPatterns("/*");
        registration.setName("logMdcFilter");
        registration.setOrder(0);
        return registration;
    }
}
