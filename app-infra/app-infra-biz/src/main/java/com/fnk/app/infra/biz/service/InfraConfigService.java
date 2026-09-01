package com.fnk.app.infra.biz.service;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fnk.app.infra.api.model.query.InfraConfigQuery;
import com.fnk.app.infra.biz.dal.entity.InfraConfigDO;
import com.fnk.app.infra.biz.dal.mapper.InfraConfigMapper;
import com.fnk.common.db.impl.BaseService;
import com.fnk.common.db.vo.PageVO;
import com.fnk.common.tools.lang.AssertUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 系统参数配置服务。
 *
 * @author Enigma
 */
@Service
public class InfraConfigService extends BaseService<InfraConfigMapper, InfraConfigDO> {

    public PageVO<InfraConfigDO> page(InfraConfigQuery query) {
        return this.basicPage(query, InfraConfigDO::getCreateTime, wrapper -> wrapper
                .like(StrUtil.isNotBlank(query.getConfigName()), InfraConfigDO::getConfigName, query.getConfigName())
                .like(StrUtil.isNotBlank(query.getConfigKey()), InfraConfigDO::getConfigKey, query.getConfigKey())
                .eq(StrUtil.isNotBlank(query.getGroupCode()), InfraConfigDO::getGroupCode, query.getGroupCode())
                .eq(query.getStatus() != null, InfraConfigDO::getStatus, query.getStatus()));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public InfraConfigDO create(InfraConfigDO req) {
        normalizeAndValidate(req, null);
        return super.create(req);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public InfraConfigDO update(String id, InfraConfigDO req) {
        normalizeAndValidate(req, id);
        return super.update(id, req);
    }

    public String value(String configKey) {
        if (StrUtil.isBlank(configKey)) {
            return null;
        }
        InfraConfigDO config = this.getFirst(new LambdaQueryWrapper<InfraConfigDO>()
                .eq(InfraConfigDO::getConfigKey, configKey)
                .eq(InfraConfigDO::getStatus, true));
        return config == null ? null : config.getConfigValue();
    }

    private void normalizeAndValidate(InfraConfigDO config, String id) {
        AssertUtils.isNull(config, "系统参数不能为空");
        config.setConfigKey(StrUtil.trim(config.getConfigKey()));
        config.setConfigName(StrUtil.trim(config.getConfigName()));
        config.setGroupCode(StrUtil.trim(config.getGroupCode()));
        config.setValueType(StrUtil.blankToDefault(StrUtil.trim(config.getValueType()), "string"));
        if (config.getVisible() == null) {
            config.setVisible(true);
        }
        if (config.getStatus() == null) {
            config.setStatus(true);
        }
        AssertUtils.isBlank(config.getConfigKey(), "参数键不能为空");
        AssertUtils.isBlank(config.getConfigName(), "参数名称不能为空");
        AssertUtils.isBlank(config.getGroupCode(), "参数分组不能为空");

        LambdaQueryWrapper<InfraConfigDO> wrapper = new LambdaQueryWrapper<InfraConfigDO>()
                .eq(InfraConfigDO::getConfigKey, config.getConfigKey());
        if (StrUtil.isNotBlank(id)) {
            wrapper.ne(InfraConfigDO::getId, id);
        }
        AssertUtils.isTrue(this.count(wrapper) > 0, "参数键已存在");
    }

    @Override
    public String getServiceModelName() {
        return "系统参数";
    }
}
