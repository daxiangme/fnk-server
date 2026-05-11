package com.fnk.common.db.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fnk.common.bean.exception.LogicException;
import com.fnk.common.db.dto.SplitPageDTO;
import com.fnk.common.db.entity.BaseEntity;
import com.fnk.common.db.interf.CustomBasicPageQuery;
import com.fnk.common.db.service.IBaseService;
import com.fnk.common.db.vo.PageVO;
import com.fnk.common.tools.lang.AssertUtils;
import com.fnk.common.tools.utils.RedisUtils;
import jakarta.annotation.Nullable;
import org.springframework.beans.BeanUtils;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * @author Enigma
 */
public class BaseService<M extends BaseMapper<T>, T extends BaseEntity<T>> extends ServiceImpl<M, T> implements IBaseService<T> {
    public final static String[] BASE_ENTITY_FIELDS = {"id", "create_time", "update_time", "deleted"};

    public final static String LIMIT_ONE = "limit 1";

    @Override
    public T getFirst(QueryWrapper<T> queryWrapper) {
        if (queryWrapper != null) {
            queryWrapper.last(LIMIT_ONE);
        }
        return this.getOne(queryWrapper);
    }

    @Override
    public T getById(Serializable id, @Nullable Consumer<LambdaQueryWrapper<T>> wrapperConsumer) {
        LambdaQueryWrapper<T> wrapper = new LambdaQueryWrapper<T>().setEntityClass(this.getEntityClass())
                .eq(T::getId, id);
        if(wrapperConsumer!=null){
            wrapperConsumer.accept(wrapper);
        }
        wrapper.last(LIMIT_ONE);
        return this.getOne(wrapper);
    }

    @Override
    public T getFirst(LambdaQueryWrapper<T> queryWrapper) {
        if (queryWrapper != null) {
            queryWrapper.last(LIMIT_ONE);
        }
        return this.getOne(queryWrapper);
    }

    @Override
    public T getByField(SFunction<T, ?> field, String value) {
        return this.getFirst(new LambdaQueryWrapper<T>().eq(field, value));
    }

    @Override
    public PageVO<T> basicPage(SplitPageDTO dto, SFunction<T, ?> orderByField, CustomBasicPageQuery<T> customQuery) {
        QueryWrapper<T> wrapper = new QueryWrapper<>();
        wrapper.like(dto.getId() != null, "id", dto.getId());
        wrapper.ge(StrUtil.isNotBlank(dto.getCreateTime()), "create_time", dto.getCreateTime());
        wrapper.ge(StrUtil.isNotBlank(dto.getUpdateTime()), "update_time", dto.getUpdateTime());
        LambdaQueryWrapper<T> lambdaQueryWrapper = wrapper.lambda();
        lambdaQueryWrapper.orderBy(orderByField != null, dto.isAsc(), orderByField);
        if (customQuery != null) {
            customQuery.query(lambdaQueryWrapper);
        }
        int pageSize = dto.getPageSize();
        if (pageSize < 0) {
            pageSize = 1000;
        }
        return new PageVO<>(this.page(new Page<>(dto.getPage(), pageSize), wrapper));
    }

    @Override
    public void resetBaseField(T t) {
        if (t != null) {
            t.setId(null);
            t.setCreateTime(null);
            t.setUpdateTime(null);
            t.setDeleted((short) 0);
        }
    }

    private LambdaQueryWrapper<T> getIdCustomQueryConsumer(String id, Consumer<LambdaQueryWrapper<T>> consumer) {
        LambdaQueryWrapper<T> wrapper = new LambdaQueryWrapper<T>().setEntityClass(this.getEntityClass())
                .eq(T::getId, id);
        if (consumer != null) {
            consumer.accept(wrapper);
        }
        return wrapper;
    }

    @Override
    public T create(T req) {
        this.resetBaseField(req);
        AssertUtils.isFalse(req.insert(), "创建" + getServiceModelName() + "失败");
        this.redisHashSet(false, req);
        return req;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<T> create(List<T> reqs) {
        reqs.forEach(this::resetBaseField);
        AssertUtils.isFalse(this.saveBatch(reqs), "批量创建" + getServiceModelName() + "失败");
        this.redisHashSet(false, reqs, null);
        return reqs;
    }

    @Override
    public T update(String id, T req) {
        return this.update(id, req, null);
    }

    @Override
    public T update(String id, T req, @Nullable Consumer<LambdaQueryWrapper<T>> query) {
        T model = this.detail(id, query);
        BeanUtils.copyProperties(req, model, BASE_ENTITY_FIELDS);
        AssertUtils.isFalse(model.updateById(), "更新" + getServiceModelName() + "失败");
        this.redisHashSet(false, req);
        return model;
    }

    @Override
    public void removeSingle(String id) {
        this.removeSingle(id, null);
    }

    @Override
    public void removeSingle(String id, @Nullable Consumer<LambdaQueryWrapper<T>> query) {
        String msg = "删除" + getServiceModelName() + "失败";
        if (query == null) {
            AssertUtils.isFalse(this.removeById(id), msg);
        } else {
            AssertUtils.isFalse(this.remove(this.getIdCustomQueryConsumer(id, query)), msg);
        }
        this.redisHashSet(true, null, CollUtil.newArrayList(id));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void remove(List<String> idList) {
        this.remove(idList, null);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void remove(List<String> idList, @Nullable Consumer<LambdaQueryWrapper<T>> query) {
        String msg = "批量" + getServiceModelName() + "删除失败";
        if (query == null) {
            AssertUtils.isFalse(this.removeBatchByIds(idList), msg);
        } else {
            LambdaQueryWrapper<T> wrapper = new LambdaQueryWrapper<T>().setEntityClass(this.getEntityClass())
                    .in(T::getId, idList);
            query.accept(wrapper);
            AssertUtils.isFalse(this.remove(wrapper), msg);
        }
        this.redisHashSet(true, null, idList);
    }

    @Override
    public T detail(String id) {
        return this.detail(id, null);
    }

    @Override
    public T detail(Consumer<LambdaQueryWrapper<T>> consumer) {
        LambdaQueryWrapper<T> wrapper = new LambdaQueryWrapper<T>().setEntityClass(this.getEntityClass());
        consumer.accept(wrapper);
        return this.getFirst(wrapper);
    }

    @Override
    public T detail(String id, @Nullable Consumer<LambdaQueryWrapper<T>> query) {
        T model;
        if (query == null) {
            model = this.redisHashGet(id);
            if (model != null) {
                return model;
            }
            model = this.getById(id);
        } else {
            model = this.getFirst(this.getIdCustomQueryConsumer(id, query));
        }
        AssertUtils.isNull(model, "目标" + getServiceModelName() + "不存在");
        return model;
    }
    private T redisHashGet(String id) {
        String cacheKey = this.getModelHashCacheKey();
        RedisUtils redisUtil = RedisUtils.getINSTANCE();
        if (cacheKey != null && redisUtil != null) {
            return redisUtil.hGet(cacheKey, id, getEntityClass());
        }
        return null;
    }


    @Override
    public T getLastByCreateTime(@Nullable CustomBasicPageQuery<T> customQuery) {
        LambdaQueryWrapper<T> wrapper = new LambdaQueryWrapper<>(getEntityClass()).orderByDesc(T::getCreateTime);
        if (customQuery != null) {
            customQuery.query(wrapper);
        }
        return this.getFirst(wrapper);
    }

    @Override
    public String getServiceModelName() {
        return "";
    }

    @Override
    public String getServiceModelCacheId(T t) {
        return t.getId();
    }



    @Override
    public String getModelHashCacheKey() {
        return null;
    }


    @Override
    public <V> PageVO<V> pageToVO(PageVO<T> page, Class<V> targetClass) {
        final PageVO<V> res = new PageVO<>();
        try {
            BeanUtils.copyProperties(page, res, "records");
            List<V> records = new ArrayList<>(page.getRecords().size());
            for (T record : page.getRecords()) {
                final V v = targetClass.newInstance();
                BeanUtils.copyProperties(record, v);
                records.add(v);
            }
            res.setRecords(records);
        } catch (Exception e) {
            log.error("分页数据转换失败", e);
            throw new LogicException("分页数据转换失败");
        }
        return res;
    }
    protected void redisHashSet(boolean remove, List<T> models, List<String> removeIdList) {
        String cacheKey = this.getModelHashCacheKey();
        RedisUtils redisUtil = RedisUtils.getINSTANCE();
        if (cacheKey != null && redisUtil != null) {
            if (CollUtil.isNotEmpty(models)) {
                for (T model : models) {
                    String cacheId = this.getServiceModelCacheId(model);
                    if (remove) {
                        redisUtil.hDel(cacheKey, cacheId);
                    } else {
                        redisUtil.hSet(cacheKey, cacheId, model);
                    }
                }
            }
            if (CollUtil.isNotEmpty(removeIdList)) {
                redisUtil.hDel(cacheKey, ArrayUtil.toArray(removeIdList, String.class));
            }
        }
    }

    private void redisHashSet(boolean remove, T model) {
        this.redisHashSet(remove, CollUtil.newArrayList(model), null);
    }
}
