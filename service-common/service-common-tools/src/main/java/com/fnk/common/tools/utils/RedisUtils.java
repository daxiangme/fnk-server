package com.fnk.common.tools.utils;

import cn.hutool.core.collection.CollUtil;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * Redis工具类
 * @author Enigma
 */

@Component
public class RedisUtils {
    /**
     * 唯一实例
     */
    @Getter
    private static RedisUtils INSTANCE;

    public RedisUtils(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @PostConstruct
    public void init() {
        INSTANCE = this;
    }

    @Getter
    private final RedisTemplate<String, Object> redisTemplate;


    /**
     * 判断key是否存在
     *
     * @param key 键
     * @return true 存在 false不存在
     */
    public boolean hasKey(String key) {
        Boolean res = redisTemplate.hasKey(key);
        if (res == null) {
            return false;
        }
        return res;
    }


    /**
     * 删除缓存
     *
     * @param key 可以传一个值 或多个
     */
    public void del(String... key) {
        if (key != null && key.length > 0) {
            if (key.length == 1) {
                redisTemplate.delete(key[0]);
            } else {
                redisTemplate.delete(Arrays.asList(key));
            }
        }
    }
    public void del(String key) {
        if (key != null ) {
            redisTemplate.delete(key);
        }
    }
    public void del(Collection<String> keys) {
        redisTemplate.delete(keys);
    }


    /**
     * 普通缓存获取
     *
     * @param key 键
     * @return 值
     */
    public Object get(String key) {
        return key == null ? null : redisTemplate.opsForValue().get(key);
    }

    /**
     * 普通缓存获取
     *
     * @param key 键
     * @return 值
     */
    public <T> T get(String key, Class<T> clazz) {
        Object res = this.get(key);
        if (res != null && res.getClass().equals(clazz)) {
            return clazz.cast(res);
        }
        return null;
    }

    /**
     * 普通缓存获取
     *
     * @param keys 键
     * @return 值
     */
    public <T> List<T> multiGet(Class<T> clazz, Collection<String> keys) {
        List<Object> res = redisTemplate.opsForValue().multiGet(keys);
        if (res != null) {
            List<T> result = new ArrayList<>();
            for (Object item : res) {
                if (item.getClass().equals(clazz)) {
                    result.add(clazz.cast(item));
                }
            }
            return result;
        }
        return null;
    }

    /**
     * @param key 键
     * @return 值
     */
    public <T extends Serializable> List<T> getList(String key, Class<T> clazz) {
        List<Object> res = this.redisTemplate.opsForList().range(key, 0, -1);
        if (CollUtil.isNotEmpty(res)) {
            List<T> finalRes = new ArrayList<>();
            for (Object re : res) {
                if (re.getClass().equals(clazz)) {
                    finalRes.add(clazz.cast(re));
                }
            }
            return finalRes;
        }
        return null;
    }

    /**
     * 设置LIST集合
     *
     * @param key
     * @param list
     * @return
     */
    public <T extends Serializable> boolean setList(String key, Collection<T> list) {
        try {
            if (list == null) {
                return false;
            }
            for (T t : list) {
                redisTemplate.opsForList().leftPush(key, t);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean lisAdd(String key, Object val) {
        try {
            redisTemplate.opsForList().leftPush(key, val);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 普通缓存放入
     *
     * @param key   键
     * @param value 值
     * @return true成功 false失败
     */
    public <T extends Serializable> boolean set(String key, T value) {
        try {
            redisTemplate.opsForValue().set(key, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 普通缓存放入并设置时间
     *
     * @param key  键
     * @param hKey 值
     * @param time 时间(秒) time要大于0 如果time小于等于0 将设置无限期
     * @return true成功 false 失败
     */
    public boolean set(String key, Serializable hKey, long time, TimeUnit timeUnit) {
        try {
            if (time > 0) {
                redisTemplate.opsForValue().set(key, hKey, time, timeUnit);
            } else {
                set(key, hKey);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    private <T> T objectToClass(Object object, Class<T> clazz) {
        if (object != null && object.getClass().equals(clazz)) {
            return clazz.cast(object);
        }
        return null;
    }

    /**
     * HashGet
     *
     * @param key  键 不能为null
     * @param hKey 项 不能为null
     * @return 值
     */
    public <T> T hGet(String key, Serializable hKey, Class<T> clazz) {
        return objectToClass(redisTemplate.opsForHash().get(key, String.valueOf(hKey)), clazz);
    }

    /**
     * 获取hashKey对应的所有键值
     *
     * @param key 键
     * @return 对应的多个键值
     */
    public <K, V> Map<K, V> hmGet(String key, Class<K> classK, Class<V> classV) {
        Map<Object, Object> entries = redisTemplate.opsForHash().entries(key);
        Map<K, V> res = new HashMap<>();
        for (Object o : entries.keySet()) {
            K k = objectToClass(o, classK);
            if (k != null) {
                V val = objectToClass(entries.get(o), classV);
                if (val != null) {
                    res.put(k, val);
                }
            }
        }
        return res;
    }

    /**
     * HashSet
     *
     * @param key 键
     * @param map 对应多个键值
     * @return true 成功 false 失败
     */
    public <K extends Serializable, V extends Serializable> boolean hmSet(String key, Map<K, V> map) {
        try {
            redisTemplate.opsForHash().putAll(key, map);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * HashSet 并设置时间
     *
     * @param key  键
     * @param map  对应多个键值
     * @param time 时间(秒)
     * @return true成功 false失败
     */
    public <T extends Serializable, E extends Serializable> boolean hmSet(String key, Map<T, E> map, long time, TimeUnit timeUnit) {
        try {
            redisTemplate.opsForHash().putAll(key, map);
            if (time > 0) {
                redisTemplate.expire(key, time, timeUnit);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 向一张hash表中放入数据,如果不存在将创建
     *
     * @param key   键
     * @param hKey  项
     * @param value 值
     * @return true 成功 false失败
     */
    public boolean hSet(String key, Serializable hKey, Object value) {
        try {
            redisTemplate.opsForHash().put(key, String.valueOf(hKey), value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 向一张hash表中放入数据,如果不存在将创建
     *
     * @param key   键
     * @param hKey  项
     * @param value 值
     * @param time  时间(秒) 注意:如果已存在的hash表有时间,这里将会替换原有的时间
     * @return true 成功 false失败
     */
    public boolean hSet(String key, Serializable hKey, Object value, long time, TimeUnit timeUnit) {
        try {
            redisTemplate.opsForHash().put(key, String.valueOf(hKey), value);
            if (time > 0) {
                redisTemplate.expire(key, time, timeUnit);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 删除hash表中的值
     *
     * @param key  键 不能为null
     * @param item 项 可以使多个 不能为null
     */
    public void hDel(String key, Object... item) {
        redisTemplate.opsForHash().delete(key, item);
    }

    /**
     * 判断hash表中是否有该项的值
     *
     * @param key  键 不能为null
     * @param item 项 不能为null
     * @return true 存在 false不存在
     */
    public boolean hHasKey(String key, Serializable item) {
        return redisTemplate.opsForHash().hasKey(key, item);
    }

    /**
     * hash递增 如果不存在,就会创建一个 并把新增后的值返回
     *
     * @param key  键
     * @param item 项
     * @param by   要增加几(大于0)
     * @return
     */
    public double hIncr(String key, String item, double by) {
        return redisTemplate.opsForHash().increment(key, item, by);
    }

    /**
     * hash递减
     *
     * @param key  键
     * @param item 项
     * @param by   要减少记(小于0)
     * @return
     */
    public double hDecr(String key, String item, double by) {
        return redisTemplate.opsForHash().increment(key, item, -by);
    }


    /**
     * 模糊删除
     *
     * @param ex
     */
    public void likeDel(String ex) {
        Set<String> keys = redisTemplate.keys(ex);
        if (CollUtil.isNotEmpty(keys)) {
            redisTemplate.delete(keys);
        }
    }

    public Set<String> keys(String ex) {
        return redisTemplate.keys(ex);
    }


    /**
     * 向指定key的set集合中添加一个值
     *
     * @param key redis的key
     * @param val set集合的值
     * @return
     */
    public boolean setAdd(String key, Object val) {
        try {
            redisTemplate.opsForSet().add(key, val);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public <T extends Serializable> List<T> getSet(String key, Class<T> clazz) {
        if (Boolean.FALSE.equals(this.redisTemplate.hasKey(key))) {
            return null;
        }
        List<Object> res = this.redisTemplate.opsForSet().pop(key, -1);
        if (CollUtil.isNotEmpty(res)) {
            List<T> finalRes = new ArrayList<>();
            for (Object re : res) {
                if (re.getClass().equals(clazz)) {
                    finalRes.add(clazz.cast(re));
                }
            }
            return finalRes;
        }
        return null;
    }

    /**
     * set集合中是否存在指定数据
     *
     * @param key
     * @param val
     * @return
     */
    public boolean setHas(String key, Object val) {
        if (hasKey(key)) {
            return Boolean.TRUE.equals(redisTemplate.opsForSet().isMember(key, val));
        } else {
            return false;
        }
    }

    public boolean streamAdd() {
        redisTemplate.opsForStream().add(null);
        return false;
    }
}
