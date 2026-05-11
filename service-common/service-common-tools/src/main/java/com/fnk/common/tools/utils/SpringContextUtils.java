package com.fnk.common.tools.utils;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * @author Enigma
 */
public class SpringContextUtils {
    private static ApplicationContext applicationContext;

    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    /**
     * 设置应用上下文
     *
     * @param context ApplicationContext
     */
    public static void setApplicationContext(ApplicationContext context) {
        if (applicationContext == null) {
            applicationContext = context;
        }
    }

    /**
     * 注册实例
     *
     * @param name 实例名称
     * @param bean 实例
     * @param <T>  类型
     */
    public static <T> void registerBean(String name, T bean) {
        ConfigurableApplicationContext context = (ConfigurableApplicationContext) applicationContext;
        context.getBeanFactory().registerSingleton(name, bean);
    }

    /**
     * 获取实例
     * @param name 实例名称
     * @param clazz 实例
     * @param <T> 类型
     * @return 实例
     */
    public static <T> T getBean(String name, Class<T> clazz) {
        ConfigurableApplicationContext context = (ConfigurableApplicationContext) applicationContext;
        return context.getBeanFactory().getBean(name, clazz);
    }

}
