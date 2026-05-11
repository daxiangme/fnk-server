package com.fnk.common.tools.lang;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.fnk.common.bean.exception.LogicException;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 *
 * 判断工具类
 * @author Enigma
 */
public class AssertUtils {
    public static void isTrue(Boolean expression, String message) {
        if (expression) {
            throw new LogicException(message);
        }
    }

    public static void isFalse(Boolean expression, String message) {
        if (!expression) {
            throw new LogicException(message);
        }
    }

    public static void isLeZero(Integer expression, String message) {
        if (expression <= 0) {
            throw new LogicException(message);
        }
    }

    public static void isNull(Object obj, String message) {
        if (obj == null || BeanUtil.isEmpty(obj)) {
            throw new LogicException(message);
        }
    }

    public static void isNotNull(Object obj, String message) {
        if (BeanUtil.isNotEmpty(obj)) {
            throw new LogicException(message);
        }
    }

    public static void isEmpty(String str, String message) {
        if (StrUtil.isEmpty(str)) {
            throw new LogicException(message);
        }
    }

    public static void isEmpty(Collection<?> collection, String message) {
        if (CollUtil.isEmpty(collection)) {
            throw new LogicException(message);
        }
    }

    public static void isEmpty(Iterable<?> iterable, String message) {
        if (CollUtil.isEmpty(iterable)) {
            throw new LogicException(message);
        }
    }

    public static void isEmpty(Map<?, ?> map, String message) {
        if (CollUtil.isEmpty(map)) {
            throw new LogicException(message);
        }
    }

    public static void isEmpty(List<?> list, String message) {
        if (CollUtil.isEmpty(list)) {
            throw new LogicException(message);
        }
    }

    public static void isBlank(String str, String message) {
        if (StrUtil.isBlank(str)) {
            throw new LogicException(message);
        }
    }
}
