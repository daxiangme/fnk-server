package com.fnk.app.system.biz.service;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fnk.app.system.api.model.query.SystemNotifyTemplateQuery;
import com.fnk.app.system.biz.dal.entity.SystemNotifyTemplateDO;
import com.fnk.app.system.biz.dal.mapper.SystemNotifyTemplateMapper;
import com.fnk.app.system.biz.service.messages.MessageJsonUtils;
import com.fnk.app.system.biz.service.messages.MessageTemplateRenderService;
import com.fnk.common.db.impl.BaseService;
import com.fnk.common.db.vo.PageVO;
import com.fnk.common.tools.lang.AssertUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 站内信模板服务。
 *
 * @author Enigma
 */
@Service
@RequiredArgsConstructor
public class SystemNotifyTemplateService extends BaseService<SystemNotifyTemplateMapper, SystemNotifyTemplateDO> {
    private final MessageTemplateRenderService renderService;

    public PageVO<SystemNotifyTemplateDO> page(SystemNotifyTemplateQuery query) {
        return this.basicPage(query, SystemNotifyTemplateDO::getCreateTime, wrapper -> wrapper
                .like(StrUtil.isNotBlank(query.getName()), SystemNotifyTemplateDO::getName, query.getName())
                .eq(StrUtil.isNotBlank(query.getCode()), SystemNotifyTemplateDO::getCode, query.getCode())
                .eq(query.getStatus() != null, SystemNotifyTemplateDO::getStatus, query.getStatus()));
    }

    public SystemNotifyTemplateDO getEnabledByCode(String code) {
        AssertUtils.isBlank(code, "模板编码不能为空");
        SystemNotifyTemplateDO template = this.getFirst(new LambdaQueryWrapper<SystemNotifyTemplateDO>()
                .eq(SystemNotifyTemplateDO::getCode, code)
                .eq(SystemNotifyTemplateDO::getStatus, true));
        AssertUtils.isNull(template, "站内信模板不存在或已停用");
        return template;
    }

    @Override
    public SystemNotifyTemplateDO create(SystemNotifyTemplateDO req) {
        normalizeAndValidate(req);
        return super.create(req);
    }

    @Override
    public SystemNotifyTemplateDO update(String id, SystemNotifyTemplateDO req) {
        req.setId(id);
        normalizeAndValidate(req);
        return super.update(id, req);
    }

    private void normalizeAndValidate(SystemNotifyTemplateDO template) {
        AssertUtils.isNull(template, "站内信模板不能为空");
        AssertUtils.isBlank(template.getName(), "模板名称不能为空");
        AssertUtils.isBlank(template.getCode(), "模板编码不能为空");
        AssertUtils.isBlank(template.getNickname(), "发送人昵称不能为空");
        AssertUtils.isBlank(template.getContent(), "模板内容不能为空");
        if (template.getStatus() == null) {
            template.setStatus(true);
        }
        List<String> params = MessageJsonUtils.readStringList(template.getParams());
        if (params.isEmpty()) {
            params = renderService.extractParams(template.getContent());
        }
        template.setParams(MessageJsonUtils.write(params));
        boolean duplicated = this.count(new LambdaQueryWrapper<SystemNotifyTemplateDO>()
                .eq(SystemNotifyTemplateDO::getCode, template.getCode())
                .ne(StrUtil.isNotBlank(template.getId()), SystemNotifyTemplateDO::getId, template.getId())) > 0;
        AssertUtils.isTrue(duplicated, "站内信模板编码不能重复");
    }
}
