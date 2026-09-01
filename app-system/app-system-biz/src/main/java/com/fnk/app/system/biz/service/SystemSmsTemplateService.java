package com.fnk.app.system.biz.service;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fnk.app.system.api.model.query.SystemSmsTemplateQuery;
import com.fnk.app.system.biz.dal.entity.SystemSmsTemplateDO;
import com.fnk.app.system.biz.dal.mapper.SystemSmsTemplateMapper;
import com.fnk.app.system.biz.service.messages.MessageJsonUtils;
import com.fnk.app.system.biz.service.messages.MessageTemplateRenderService;
import com.fnk.common.db.impl.BaseService;
import com.fnk.common.db.vo.PageVO;
import com.fnk.common.tools.lang.AssertUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 短信模板服务。
 *
 * @author Enigma
 */
@Service
@RequiredArgsConstructor
public class SystemSmsTemplateService extends BaseService<SystemSmsTemplateMapper, SystemSmsTemplateDO> {
    private final SystemSmsChannelService channelService;
    private final MessageTemplateRenderService renderService;

    public PageVO<SystemSmsTemplateDO> page(SystemSmsTemplateQuery query) {
        return this.basicPage(query, SystemSmsTemplateDO::getCreateTime, wrapper -> wrapper
                .eq(StrUtil.isNotBlank(query.getChannelId()), SystemSmsTemplateDO::getChannelId, query.getChannelId())
                .like(StrUtil.isNotBlank(query.getTemplateName()), SystemSmsTemplateDO::getTemplateName, query.getTemplateName())
                .eq(StrUtil.isNotBlank(query.getTemplateCode()), SystemSmsTemplateDO::getTemplateCode, query.getTemplateCode())
                .eq(query.getStatus() != null, SystemSmsTemplateDO::getStatus, query.getStatus()));
    }

    public SystemSmsTemplateDO getEnabledByCode(String templateCode) {
        AssertUtils.isBlank(templateCode, "模板编码不能为空");
        SystemSmsTemplateDO template = this.getFirst(new LambdaQueryWrapper<SystemSmsTemplateDO>()
                .eq(SystemSmsTemplateDO::getTemplateCode, templateCode)
                .eq(SystemSmsTemplateDO::getStatus, true));
        AssertUtils.isNull(template, "短信模板不存在或已停用");
        return template;
    }

    @Override
    public SystemSmsTemplateDO create(SystemSmsTemplateDO req) {
        normalizeAndValidate(req);
        return super.create(req);
    }

    @Override
    public SystemSmsTemplateDO update(String id, SystemSmsTemplateDO req) {
        req.setId(id);
        normalizeAndValidate(req);
        return super.update(id, req);
    }

    private void normalizeAndValidate(SystemSmsTemplateDO template) {
        AssertUtils.isNull(template, "短信模板不能为空");
        AssertUtils.isBlank(template.getChannelId(), "短信渠道不能为空");
        AssertUtils.isBlank(template.getTemplateName(), "模板名称不能为空");
        AssertUtils.isBlank(template.getTemplateCode(), "模板编码不能为空");
        AssertUtils.isBlank(template.getContent(), "模板内容不能为空");
        channelService.detail(template.getChannelId());
        if (template.getStatus() == null) {
            template.setStatus(true);
        }
        List<String> params = MessageJsonUtils.readStringList(template.getParams());
        if (params.isEmpty()) {
            params = renderService.extractParams(template.getContent());
        }
        template.setParams(MessageJsonUtils.write(params));
        boolean duplicated = this.count(new LambdaQueryWrapper<SystemSmsTemplateDO>()
                .eq(SystemSmsTemplateDO::getTemplateCode, template.getTemplateCode())
                .ne(StrUtil.isNotBlank(template.getId()), SystemSmsTemplateDO::getId, template.getId())) > 0;
        AssertUtils.isTrue(duplicated, "短信模板编码不能重复");
    }
}
