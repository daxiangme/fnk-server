package com.fnk.app.system.biz.service.messages;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fnk.common.bean.exception.LogicException;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 消息中心 JSON 字段转换工具。
 *
 * @author Enigma
 */
public final class MessageJsonUtils {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private static final TypeReference<List<String>> STRING_LIST = new TypeReference<>() {
    };
    private static final TypeReference<Map<String, String>> STRING_MAP = new TypeReference<>() {
    };

    private MessageJsonUtils() {
    }

    public static String write(Object value) {
        if (value == null) {
            return null;
        }
        try {
            return OBJECT_MAPPER.writeValueAsString(value);
        } catch (Exception ex) {
            throw new LogicException("消息参数序列化失败");
        }
    }

    public static List<String> readStringList(String value) {
        if (StrUtil.isBlank(value)) {
            return List.of();
        }
        try {
            List<String> params = OBJECT_MAPPER.readValue(value, STRING_LIST);
            return normalizeStringList(params);
        } catch (Exception ex) {
            throw new LogicException("消息参数解析失败");
        }
    }

    public static Map<String, String> readStringMap(String value) {
        if (StrUtil.isBlank(value)) {
            return Map.of();
        }
        try {
            Map<String, String> params = OBJECT_MAPPER.readValue(value, STRING_MAP);
            return params == null ? Map.of() : params;
        } catch (Exception ex) {
            throw new LogicException("消息参数解析失败");
        }
    }

    public static List<String> normalizeStringList(List<String> params) {
        if (CollUtil.isEmpty(params)) {
            return List.of();
        }
        List<String> result = new ArrayList<>();
        for (String param : params) {
            if (StrUtil.isBlank(param) || result.contains(param.trim())) {
                continue;
            }
            result.add(param.trim());
        }
        return result;
    }

    public static Map<String, String> normalizeStringMap(Map<String, String> params) {
        if (params == null || params.isEmpty()) {
            return Map.of();
        }
        Map<String, String> result = new LinkedHashMap<>();
        params.forEach((key, value) -> {
            if (StrUtil.isNotBlank(key)) {
                result.put(key.trim(), value);
            }
        });
        return result;
    }
}
