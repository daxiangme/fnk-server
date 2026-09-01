package com.fnk.app.system.biz.service;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fnk.app.system.api.model.query.SystemDictItemQuery;
import com.fnk.app.system.biz.dal.entity.SystemDictItemDO;
import com.fnk.app.system.biz.dal.mapper.SystemDictItemMapper;
import com.fnk.common.db.impl.BaseService;
import com.fnk.common.db.vo.PageVO;
import com.fnk.common.tools.lang.AssertUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 系统字典项服务。
 *
 * @author Enigma
 */
@Service
public class SystemDictItemService extends BaseService<SystemDictItemMapper, SystemDictItemDO> {

    public PageVO<SystemDictItemDO> page(SystemDictItemQuery query) {
        return this.basicPage(query, SystemDictItemDO::getOrderSort, wrapper -> wrapper
                .eq(StrUtil.isNotBlank(query.getDictCode()), SystemDictItemDO::getDictCode, query.getDictCode())
                .like(StrUtil.isNotBlank(query.getLabel()), SystemDictItemDO::getLabel, query.getLabel())
                .eq(StrUtil.isNotBlank(query.getValue()), SystemDictItemDO::getValue, query.getValue())
                .eq(query.getStatus() != null, SystemDictItemDO::getStatus, query.getStatus()));
    }

    public List<SystemDictItemDO> listEnabledByDictCode(String dictCode) {
        AssertUtils.isBlank(dictCode, "字典编码不能为空");
        return this.list(new LambdaQueryWrapper<SystemDictItemDO>()
                .eq(SystemDictItemDO::getDictCode, dictCode)
                .eq(SystemDictItemDO::getStatus, true)
                .orderByAsc(SystemDictItemDO::getOrderSort));
    }

    @Override
    public SystemDictItemDO create(SystemDictItemDO req) {
        normalizeAndValidate(req);
        return super.create(req);
    }

    @Override
    public SystemDictItemDO update(String id, SystemDictItemDO req) {
        req.setId(id);
        normalizeAndValidate(req);
        return super.update(id, req);
    }

    public void deleteByDictCode(String dictCode) {
        if (StrUtil.isBlank(dictCode)) {
            return;
        }
        this.remove(new LambdaQueryWrapper<SystemDictItemDO>().eq(SystemDictItemDO::getDictCode, dictCode));
    }

    private void normalizeAndValidate(SystemDictItemDO item) {
        AssertUtils.isNull(item, "字典项不能为空");
        AssertUtils.isBlank(item.getDictCode(), "字典编码不能为空");
        AssertUtils.isBlank(item.getLabel(), "字典标签不能为空");
        AssertUtils.isBlank(item.getValue(), "字典值不能为空");
        if (item.getOrderSort() == null) {
            item.setOrderSort(0);
        }
        if (item.getStatus() == null) {
            item.setStatus(true);
        }
        boolean duplicated = this.count(new LambdaQueryWrapper<SystemDictItemDO>()
                .eq(SystemDictItemDO::getDictCode, item.getDictCode())
                .eq(SystemDictItemDO::getValue, item.getValue())
                .ne(StrUtil.isNotBlank(item.getId()), SystemDictItemDO::getId, item.getId())) > 0;
        AssertUtils.isTrue(duplicated, "同一字典下字典值不能重复");
    }
}
