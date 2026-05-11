package com.fnk.starter.web.config;

import cn.hutool.core.util.StrUtil;
import com.fnk.common.bean.consts.EnvConst;
import com.fnk.common.tools.utils.SpringContextUtils;
import lombok.Getter;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import java.util.TimeZone;

/**
 * @author Enigma
 */
@Configuration
@Order(-1)
public class BasicConfig implements ApplicationContextAware {
    private static String ENV_TAG;

    @Getter
    private static String CURRENT_API_VERSION;


    @Value("${spring.application.name}")
    private String applicationName;

    @Value("${service.api-version}")
    private String apiVersion = "1";

    @Bean("clientTag")
    public String clientTag() {
        String[] names = applicationName.split("-");
        return names[names.length - 1].toUpperCase();
    }

    static {
        ENV_TAG = System.getenv("SERVICE_ENV");
        if (StrUtil.isEmpty(ENV_TAG)) {
            ENV_TAG = EnvConst.DEV;
        } else {
            ENV_TAG = ENV_TAG.toLowerCase();
        }
        TimeZone.setDefault(TimeZone.getTimeZone("GMT+08:00"));
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        SpringContextUtils.setApplicationContext(applicationContext);
        CURRENT_API_VERSION = apiVersion;
    }

    public static String getEnv() {
        return ENV_TAG;
    }
}
