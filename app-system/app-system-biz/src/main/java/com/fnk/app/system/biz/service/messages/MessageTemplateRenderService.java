package com.fnk.app.system.biz.service.messages;

import cn.hutool.core.util.StrUtil;
import com.fnk.common.tools.lang.AssertUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 消息模板渲染服务，统一处理 {param} 占位符。
 *
 * @author Enigma
 */
@Service
public class MessageTemplateRenderService {
    private static final Pattern PARAM_PATTERN = Pattern.compile("\\{([a-zA-Z0-9_]+)}");

    public List<String> extractParams(String content) {
        if (StrUtil.isBlank(content)) {
            return List.of();
        }
        Matcher matcher = PARAM_PATTERN.matcher(content);
        List<String> params = new ArrayList<>();
        while (matcher.find()) {
            String param = matcher.group(1);
            if (!params.contains(param)) {
                params.add(param);
            }
        }
        return params;
    }

    public String render(String content, Map<String, String> params) {
        if (StrUtil.isBlank(content)) {
            return content;
        }
        Map<String, String> safeParams = MessageJsonUtils.normalizeStringMap(params);
        Matcher matcher = PARAM_PATTERN.matcher(content);
        StringBuilder result = new StringBuilder();
        while (matcher.find()) {
            String paramName = matcher.group(1);
            String paramValue = safeParams.get(paramName);
            AssertUtils.isBlank(paramValue, "模板参数 " + paramName + " 不能为空");
            matcher.appendReplacement(result, Matcher.quoteReplacement(paramValue));
        }
        matcher.appendTail(result);
        return result.toString();
    }
}
