package com.fnk.app.system.biz.service;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fnk.app.system.api.model.query.SystemSmsChannelQuery;
import com.fnk.app.system.biz.dal.entity.SystemSmsChannelDO;
import com.fnk.app.system.biz.dal.mapper.SystemSmsChannelMapper;
import com.fnk.common.db.impl.BaseService;
import com.fnk.common.db.vo.PageVO;
import com.fnk.common.tools.lang.AssertUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;

/**
 * 短信渠道服务。
 *
 * @author Enigma
 */
@Service
public class SystemSmsChannelService extends BaseService<SystemSmsChannelMapper, SystemSmsChannelDO> {

    public PageVO<SystemSmsChannelDO> page(SystemSmsChannelQuery query) {
        return this.basicPage(query, SystemSmsChannelDO::getCreateTime, wrapper -> wrapper
                .like(StrUtil.isNotBlank(query.getChannelName()), SystemSmsChannelDO::getChannelName, query.getChannelName())
                .eq(StrUtil.isNotBlank(query.getChannelCode()), SystemSmsChannelDO::getChannelCode, query.getChannelCode())
                .eq(query.getStatus() != null, SystemSmsChannelDO::getStatus, query.getStatus()));
    }

    public List<SystemSmsChannelDO> listEnabled() {
        return this.list(new LambdaQueryWrapper<SystemSmsChannelDO>()
                .eq(SystemSmsChannelDO::getStatus, true)
                .orderByDesc(SystemSmsChannelDO::getCreateTime));
    }

    @Override
    public SystemSmsChannelDO create(SystemSmsChannelDO req) {
        normalizeAndValidate(req);
        return super.create(req);
    }

    @Override
    public SystemSmsChannelDO update(String id, SystemSmsChannelDO req) {
        req.setId(id);
        normalizeAndValidate(req);
        return super.update(id, req);
    }

    private void normalizeAndValidate(SystemSmsChannelDO channel) {
        AssertUtils.isNull(channel, "短信渠道不能为空");
        AssertUtils.isBlank(channel.getChannelName(), "渠道名称不能为空");
        AssertUtils.isBlank(channel.getChannelCode(), "渠道编码不能为空");
        channel.setChannelCode(channel.getChannelCode().trim().toUpperCase(Locale.ROOT));
        if (channel.getStatus() == null) {
            channel.setStatus(true);
        }
        boolean duplicated = this.count(new LambdaQueryWrapper<SystemSmsChannelDO>()
                .eq(SystemSmsChannelDO::getChannelCode, channel.getChannelCode())
                .ne(StrUtil.isNotBlank(channel.getId()), SystemSmsChannelDO::getId, channel.getId())) > 0;
        AssertUtils.isTrue(duplicated, "短信渠道编码不能重复");
    }
}
