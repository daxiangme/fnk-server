package com.fnk.starter.web.converter;

import cn.hutool.core.map.MapUtil;
import com.fnk.starter.web.interf.IBaseEnum;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.ConverterFactory;

import java.util.Map;

/**
 * 字符串转换为枚举转换器工厂
 *
 * @author Enigma
 */
public class StringToEnumConverterFactory implements ConverterFactory<String, IBaseEnum> {
    private static final Map<Class, Converter> CONVERTERS = MapUtil.newHashMap();

    @Override
    public <T extends IBaseEnum> Converter<String, T> getConverter(Class<T> targetType) {
        Converter<String, T> converter = CONVERTERS.get(targetType);
        if (converter == null) {
            converter = new StringToEnumConverter<>(targetType);
            CONVERTERS.put(targetType, converter);
        }
        return converter;
    }
}

class StringToEnumConverter<T extends IBaseEnum> implements Converter<String, T> {

    private final Class<T> enumType;


    public StringToEnumConverter(Class<T> enumType) {
        this.enumType = enumType;
    }

    @Override
    public T convert(String source) {
        for (T e : enumType.getEnumConstants()) {
            if (e.getValue().toString().equals(source)) {
                return e;
            }
        }
        return null;
    }
}
