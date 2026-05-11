package com.fnk.starter.web.converter;

import cn.hutool.core.map.MapUtil;
import com.fnk.starter.web.interf.IBaseEnum;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.ConverterFactory;

import java.util.Map;

/**
 * 整数转换为枚举转换器工厂
 * @author Enigma
 */
public class IntegerToEnumConverterFactory implements ConverterFactory<Integer, IBaseEnum> {
    private static final Map<Class, Converter> CONVERTERS = MapUtil.newHashMap();

    @Override
    public <T extends IBaseEnum> Converter<Integer, T> getConverter(Class<T> targetType) {
        Converter<Integer, T> converter = CONVERTERS.get(targetType);
        if (converter == null) {
            converter = new IntegerToEnumConverter<>(targetType);
            CONVERTERS.put(targetType, converter);
        }
        return converter;
    }
}

class IntegerToEnumConverter<T extends IBaseEnum> implements Converter<Integer, T> {

    private final Class<T> enumType;


    public IntegerToEnumConverter(Class<T> enumType) {
        this.enumType = enumType;
    }

    @Override
    public T convert(Integer source) {
        for (T e : enumType.getEnumConstants()) {
            if (e.getValue().equals(source)) {
                return e;
            }
        }
        return null;
    }
}
