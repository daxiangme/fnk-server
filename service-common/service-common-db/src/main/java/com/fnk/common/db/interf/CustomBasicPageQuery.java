package com.fnk.common.db.interf;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;

/**
 * @author Enigma
 */
public interface CustomBasicPageQuery<T> {
    /**
     * 自定义查询
     *
     * @param wrapper 条件包装
     */
    void query(LambdaQueryWrapper<T> wrapper);
}
