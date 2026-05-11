package com.fnk.common.tools.utils;

import java.util.Map;

/**
 * xml工具类
 * <p>map转xml</p>
 * @author Enigma
 */
public class XmlUtils {
    public static String mapToXml(Map<?, ?> map) {
        StringBuffer sb = new StringBuffer();
        sb.append("<xml>");
        map.forEach((key, value) -> {
            sb.append("<").append(key).append(">");
            sb.append(value);
            sb.append("</").append(key).append(">");
        });
        sb.append("</xml>");
        return sb.toString();
    }
}
