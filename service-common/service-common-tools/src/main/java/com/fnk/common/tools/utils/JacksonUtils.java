package com.fnk.common.tools.utils;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author Enigma
 */
public class JacksonUtils {
    @Getter
    private final static ObjectMapper INSTANCE;

    static {
        INSTANCE = new ObjectMapper();
        INSTANCE.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public static String toJsonString(Object obj) {
        try {
            return INSTANCE.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static <T> T parseObject(String value, Class<T> clazz) {
        try {
            return INSTANCE.readValue(value, clazz);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static <T> List<T> parseLongArray(String value, Class<T> clazz) {
        if (StrUtil.isEmpty(value)) {
            return new ArrayList<>();
        }
        try {
            return INSTANCE.readValue(value, CollectionsTypeFactory.erasedListOf(clazz));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public static HashMap<String,String> parseMapObject(String json) {
        try {
            return JSONUtil.toBean(json, new cn.hutool.core.lang.TypeReference<HashMap<String, String>>() {
            }, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    static class CollectionsTypeFactory {
        static <T> TypeReference<List> erasedListOf(Class<T> ignored) {
            return new TypeReference<List>(){};
        }
    }

}
