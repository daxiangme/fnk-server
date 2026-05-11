package com.fnk.starter.web.filter;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.fnk.common.bean.consts.BaseConst;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.MDC;

import java.io.IOException;

/**
 * 主要进行请求ID设置和移除
 * @author Enigma
 */
public class LogMdcFilter implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        String requestId = IdUtil.getSnowflakeNextIdStr();
        Object loginId = null;
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        String authorization = request.getHeader(BaseConst.HEADER_AUTHORIZATION);
        if (StrUtil.isNotEmpty(authorization)) {
            try {
                loginId = StpUtil.getLoginIdByToken(authorization);
            } catch (Exception ignored) {
            }
        }
        try {
            MDC.put("requestId", requestId);
            if (loginId != null) {
                MDC.put("uid", String.valueOf(loginId));
            }
            filterChain.doFilter(servletRequest, servletResponse);
        } finally {
            MDC.remove("requestId");
            if (loginId != null) {
                MDC.remove("uid");
            }
        }
    }
}
