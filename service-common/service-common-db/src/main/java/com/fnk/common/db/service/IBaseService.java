package com.fnk.common.db.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fnk.common.db.dto.SplitPageDTO;
import com.fnk.common.db.entity.BaseEntity;
import com.fnk.common.db.interf.CustomBasicPageQuery;
import com.fnk.common.db.vo.PageVO;
import jakarta.annotation.Nullable;

import java.io.Serializable;
import java.util.List;
import java.util.function.Consumer;

/**
 * @author Enigma
 */
public interface IBaseService<T extends BaseEntity<T>> extends IService<T> {

    T getById(Serializable id, @Nullable Consumer<LambdaQueryWrapper<T>> wrapperConsumer);

    /**
     * 获取第一条记录
     *
     * @param queryWrapper 查询条件
     * @return 查询结果
     */
    T getFirst(LambdaQueryWrapper<T> queryWrapper);

    /**
     * 获取第一条记录
     *
     * @param queryWrapper 查询条件
     * @return 查询结果
     * @see IBaseService#getFirst(LambdaQueryWrapper)
     */
    T getFirst(QueryWrapper<T> queryWrapper);


    /**
     * 根据字段匹配获取第一条数据 <br>
     * 例如：用户名在全局是唯一的，则为getByField(User::getUsername, username)
     *
     * @param field 字段
     * @param value 字段值
     * @return 记录
     */
    T getByField(SFunction<T, ?> field, String value);

    /**
     * 基础分页查询方法
     *
     * @param dto          查询数据
     * @param orderByField 排序字段
     * @param customQuery  自定义查询条件组合
     * @return 分页查询数据
     */
    PageVO<T> basicPage(SplitPageDTO dto, SFunction<T, ?> orderByField, CustomBasicPageQuery<T> customQuery);

    /**
     * 重置基础字段
     *
     * @param t 对象
     */
    void resetBaseField(T t);

    /**
     * 创建新的数据
     *
     * @param req 数据体
     * @return 数据
     */
    T create(T req);

    /**
     * 批量创建新的数据
     *
     * @param reqs 数据体
     * @return 数据
     */
    List<T> create(List<T> reqs);

    /**
     * 更新数据
     *
     * @param id  ID
     * @param req 数据体
     * @return 数据
     */
    T update(String id, T req);


    /**
     * 更新数据
     *
     * @param id    ID
     * @param req   数据体
     * @param query 附加查询条件
     * @return 数据
     */
    T update(String id, T req, @Nullable Consumer<LambdaQueryWrapper<T>> query);

    /**
     * 删除数据
     *
     * @param id ID
     */
    void removeSingle(String id);

    /**
     * 删除数据
     *
     * @param query 附加查询条件
     * @param id    ID
     */
    void removeSingle(String id, @Nullable Consumer<LambdaQueryWrapper<T>> query);

    /**
     * 批量删除数据
     *
     * @param idList ID列表
     */
    void remove(List<String> idList);

    /**
     * 批量删除数据
     *
     * @param idList ID列表
     * @param query  附加查询条件
     */
    void remove(List<String> idList, @Nullable Consumer<LambdaQueryWrapper<T>> query);

    /**
     * 查询指定ID数据的详情
     *
     * @param id ID
     * @return 数据
     */
    T detail(String id);

    /**
     * 查询指定数据的详情
     *
     * @return 数据
     */
    T detail(Consumer<LambdaQueryWrapper<T>> consumer);

    /**
     * 查询指定ID数据的详情
     *
     * @param id    ID
     * @param query 附加查询条件
     * @return 数据
     */
    T detail(String id, @Nullable Consumer<LambdaQueryWrapper<T>> query);



    /**
     * 获取最后一条记录根据创建时间
     *
     * @param customQuery 自定义查询
     * @return 记录
     */
    T getLastByCreateTime(@Nullable CustomBasicPageQuery<T> customQuery);

    /**
     * 获取服务模型名称
     *
     * @return 模型名称
     */
    String getServiceModelName();

    /**
     * 获取服务模型的缓存ID
     *
     * @param t 模型
     * @return 缓存ID
     */
    String getServiceModelCacheId(T t);

    /**
     * 获取模型hash存储的缓存建值
     * 当此值不为null时，为开启缓存
     *
     * @return 是否开启
     */
    String getModelHashCacheKey();





    /**
     * 分页数据转换为VO分页数据
     *
     * @param page        分页数据结构
     * @param targetClass 目标类
     * @param <V>         类型
     * @return VO分页数据结构
     */
    <V> PageVO<V> pageToVO(PageVO<T> page, Class<V> targetClass);
}
