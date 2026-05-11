package com.fnk.starter.security.config;

import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.strategy.SaStrategy;
import com.fnk.starter.security.utils.StpAdminUtil;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.AnnotatedElementUtils;

/**
 * @author Enigma
 */
@Configuration
public class SaTokenConfigure {
    @Autowired
    private String clientTag;

    @Autowired
    public void rewriteSaStrategy() {
        // 重写Sa-Token的注解处理器，增加注解合并功能
        SaStrategy.me.getAnnotation = AnnotatedElementUtils::getMergedAnnotation;
    }

    @PostConstruct
    public void initStpLogic() {
        switch (clientTag) {
            case "ADMIN" -> StpUtil.setStpLogic(StpAdminUtil.stpLogic);
            // 扩展其他客户端
            default -> {
            }
        }
    }

}
