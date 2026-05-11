package com.fnk.starter.web.converter;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.ConverterFactory;

import java.util.Date;

/**
 * @author Enigma
 */
public class DateConverterFactory implements ConverterFactory<String, Date> {
    @Override
    public <T extends Date> Converter<String, T> getConverter(Class<T> targetType) {
        return source -> {
            if (targetType == Date.class) {
                DateTime parse = DateUtil.parse(source);
                return (T) parse.toJdkDate();
            }
            return null;
        };
    }
}
