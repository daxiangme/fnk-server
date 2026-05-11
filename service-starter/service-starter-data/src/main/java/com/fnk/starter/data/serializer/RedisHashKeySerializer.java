package com.fnk.starter.data.serializer;

import cn.hutool.core.util.ObjectUtil;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

/**
 * redis哈希键值序列化
 * @author Enigma
 */
public class RedisHashKeySerializer implements RedisSerializer<Object> {

    @Override
    public byte[] serialize(Object o) throws SerializationException {
        String s = ObjectUtil.toString(o);
        return s.getBytes();
    }

    @Override
    public Object deserialize(byte[] bytes) throws SerializationException {
        return new String(bytes);
    }
}
