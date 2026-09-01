package com.fnk.app.system.biz.service;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fnk.app.system.api.model.query.SystemDictTypeQuery;
import com.fnk.app.system.biz.dal.entity.SystemDictTypeDO;
import com.fnk.app.system.biz.dal.mapper.SystemDictTypeMapper;
import com.fnk.common.db.impl.BaseService;
import com.fnk.common.db.vo.PageVO;
import com.fnk.common.tools.lang.AssertUtils;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 系统字典类型服务。
 *
 * @author Enigma
 */
@Service
@AllArgsConstructor
public class SystemDictTypeService extends BaseService<SystemDictTypeMapper, SystemDictTypeDO> {

    private final SystemDictItemService dictItemService;

    public PageVO<SystemDictTypeDO> page(SystemDictTypeQuery query) {
        return this.basicPage(query, SystemDictTypeDO::getCreateTime, wrapper -> wrapper
                .like(StrUtil.isNotBlank(query.getDictCode()), SystemDictTypeDO::getDictCode, query.getDictCode())
                .like(StrUtil.isNotBlank(query.getDictName()), SystemDictTypeDO::getDictName, query.getDictName())
                .eq(query.getStatus() != null, SystemDictTypeDO::getStatus, query.getStatus()));
    }

    public List<SystemDictTypeDO> listAll() {
        return this.list(new LambdaQueryWrapper<SystemDictTypeDO>().orderByAsc(SystemDictTypeDO::getDictCode));
    }

    @Override
    public SystemDictTypeDO create(SystemDictTypeDO req) {
        normalizeAndValidate(req);
        return super.create(req);
    }

    @Override
    public SystemDictTypeDO update(String id, SystemDictTypeDO req) {
        req.setId(id);
        normalizeAndValidate(req);
        return super.update(id, req);
    }

    @Transactional(rollbackFor = Exception.class)
    public void removeType(String id) {
        SystemDictTypeDO dictType = this.detail(id);
        dictItemService.deleteByDictCode(dictType.getDictCode());
        this.removeSingle(id);
    }

    private void normalizeAndValidate(SystemDictTypeDO dictType) {
        AssertUtils.isNull(dictType, "字典类型不能为空");
        AssertUtils.isBlank(dictType.getDictCode(), "字典编码不能为空");
        AssertUtils.isBlank(dictType.getDictName(), "字典名称不能为空");
        if (dictType.getStatus() == null) {
            dictType.setStatus(true);
        }
        boolean duplicated = this.count(new LambdaQueryWrapper<SystemDictTypeDO>()
                .eq(SystemDictTypeDO::getDictCode, dictType.getDictCode())
                .ne(StrUtil.isNotBlank(dictType.getId()), SystemDictTypeDO::getId, dictType.getId())) > 0;
        AssertUtils.isTrue(duplicated, "字典编码已存在");
    }
}
