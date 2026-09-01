package com.fnk.app.system.biz.service;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fnk.app.system.api.model.query.SystemMailTemplateQuery;
import com.fnk.app.system.biz.dal.entity.SystemMailTemplateDO;
import com.fnk.app.system.biz.dal.mapper.SystemMailTemplateMapper;
import com.fnk.app.system.biz.service.messages.MessageJsonUtils;
import com.fnk.app.system.biz.service.messages.MessageTemplateRenderService;
import com.fnk.common.db.impl.BaseService;
import com.fnk.common.db.vo.PageVO;
import com.fnk.common.tools.lang.AssertUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 邮件模板服务。
 *
 * @author Enigma
 */
@Service
@RequiredArgsConstructor
public class SystemMailTemplateService extends BaseService<SystemMailTemplateMapper, SystemMailTemplateDO> {
    private final SystemMailAccountService accountService;
    private final MessageTemplateRenderService renderService;

    public PageVO<SystemMailTemplateDO> page(SystemMailTemplateQuery query) {
        return this.basicPage(query, SystemMailTemplateDO::getCreateTime, wrapper -> wrapper
                .eq(StrUtil.isNotBlank(query.getAccountId()), SystemMailTemplateDO::getAccountId, query.getAccountId())
                .like(StrUtil.isNotBlank(query.getName()), SystemMailTemplateDO::getName, query.getName())
                .eq(StrUtil.isNotBlank(query.getCode()), SystemMailTemplateDO::getCode, query.getCode())
                .eq(query.getStatus() != null, SystemMailTemplateDO::getStatus, query.getStatus()));
    }

    public SystemMailTemplateDO getEnabledByCode(String code) {
        AssertUtils.isBlank(code, "模板编码不能为空");
        SystemMailTemplateDO template = this.getFirst(new LambdaQueryWrapper<SystemMailTemplateDO>()
                .eq(SystemMailTemplateDO::getCode, code)
                .eq(SystemMailTemplateDO::getStatus, true));
        AssertUtils.isNull(template, "邮件模板不存在或已停用");
        return template;
    }

    @Override
    public SystemMailTemplateDO create(SystemMailTemplateDO req) {
        normalizeAndValidate(req);
        return super.create(req);
    }

    @Override
    public SystemMailTemplateDO update(String id, SystemMailTemplateDO req) {
        req.setId(id);
        normalizeAndValidate(req);
        return super.update(id, req);
    }

    private void normalizeAndValidate(SystemMailTemplateDO template) {
        AssertUtils.isNull(template, "邮件模板不能为空");
        AssertUtils.isBlank(template.getAccountId(), "邮箱账号不能为空");
        AssertUtils.isBlank(template.getName(), "模板名称不能为空");
        AssertUtils.isBlank(template.getCode(), "模板编码不能为空");
        AssertUtils.isBlank(template.getTitle(), "邮件标题不能为空");
        AssertUtils.isBlank(template.getContent(), "邮件内容不能为空");
        accountService.detail(template.getAccountId());
        if (template.getStatus() == null) {
            template.setStatus(true);
        }
        List<String> params = MessageJsonUtils.readStringList(template.getParams());
        if (params.isEmpty()) {
            params = renderService.extractParams(template.getTitle() + template.getContent());
        }
        template.setParams(MessageJsonUtils.write(params));
        boolean duplicated = this.count(new LambdaQueryWrapper<SystemMailTemplateDO>()
                .eq(SystemMailTemplateDO::getCode, template.getCode())
                .ne(StrUtil.isNotBlank(template.getId()), SystemMailTemplateDO::getId, template.getId())) > 0;
        AssertUtils.isTrue(duplicated, "邮件模板编码不能重复");
    }
}
