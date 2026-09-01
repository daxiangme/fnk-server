package com.fnk.starter.security.config;

import cn.dev33.satoken.interceptor.SaInterceptor;
import cn.dev33.satoken.router.SaRouter;
import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.strategy.SaStrategy;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ArrayUtil;
import com.alibaba.fastjson2.JSONReader;
import com.alibaba.fastjson2.JSONWriter;
import com.alibaba.fastjson2.support.config.FastJsonConfig;
import com.alibaba.fastjson2.support.spring6.http.converter.FastJsonHttpMessageConverter;
import com.fnk.starter.security.annotation.AnonymousApi;
import com.fnk.starter.web.converter.DateConverterFactory;
import com.fnk.starter.web.converter.IntegerToEnumConverterFactory;
import com.fnk.starter.web.converter.StringToEnumConverterFactory;
import com.fnk.starter.web.interf.ApiInterceptors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.http.MediaType;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;

/**
 * @author Enigma
 */
@Configuration
@EnableWebMvc
public class SecurityWebMvcConfig implements WebMvcConfigurer {

    @Autowired(required = false)
    private ApiInterceptors apiInterceptors;

    @Autowired
    private String clientTag;

    public final static List<String> NOT_MATCHES = CollectionUtil.newArrayList(
            "/error",
            "/static/**",
            "/wx/portal/**",
            "/doc.html",
            "/api-doc",
            "/swagger-ui/**",
            "/webjars/**",
            "/swagger-resources/**",
            "/v3/**",
            "/favicon.ico"
    );

    public static void dynamicAddInterceptorNotMatches(List<String> paths) {
        if (ArrayUtil.isNotEmpty(paths)) {
            NOT_MATCHES.addAll(paths);
        }
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        if (apiInterceptors != null && CollectionUtil.isNotEmpty(apiInterceptors.notMatches())) {
            CollectionUtil.addAll(NOT_MATCHES, apiInterceptors.notMatches());
        }
        registry.addInterceptor(new SaInterceptor( handler -> {
            if (handler instanceof HandlerMethod) {
                Method method = ((HandlerMethod) handler).getMethod();
                if (method.getAnnotation(AnonymousApi.class) != null || method.getDeclaringClass().getAnnotation(AnonymousApi.class) != null) {
                    // 终止
                    SaRouter.stop();
                }
                SaRouter.match("/**").notMatch(NOT_MATCHES).check(r -> StpUtil.checkLogin())
                        .check(r -> SaStrategy.me.checkMethodAnnotation.accept(method));
            } else {
                SaRouter.match("/**").notMatch(NOT_MATCHES).check(r -> StpUtil.checkLogin());
            }
        })).addPathPatterns("/**");
    }

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverterFactory(new DateConverterFactory());
        registry.addConverterFactory(new StringToEnumConverterFactory());
        registry.addConverterFactory(new IntegerToEnumConverterFactory());
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("swagger-ui.html", "doc.html").addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
        registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");
        registry.addResourceHandler("/mp/**").addResourceLocations("classpath:/static/");
    }

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.add(new ByteArrayHttpMessageConverter());

        FastJsonHttpMessageConverter jsonHttpMessageConverter = new FastJsonHttpMessageConverter();
        FastJsonConfig fastJsonConfig = new FastJsonConfig();
        fastJsonConfig.setDateFormat("yyyy-MM-dd HH:mm:ss");
        fastJsonConfig.setWriterFeatures(JSONWriter.Feature.PrettyFormat);
        fastJsonConfig.setReaderFeatures(JSONReader.Feature.FieldBased, JSONReader.Feature.SupportArrayToBean);
        jsonHttpMessageConverter.setSupportedMediaTypes(Collections.singletonList(MediaType.APPLICATION_JSON));
        jsonHttpMessageConverter.setDefaultCharset(StandardCharsets.UTF_8);
        jsonHttpMessageConverter.setFastJsonConfig(fastJsonConfig);
        converters.add(jsonHttpMessageConverter);
    }

}
