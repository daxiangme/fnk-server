package com.fnk.app.system.biz.service;

import cn.hutool.core.collection.CollUtil;
import com.fnk.app.system.api.model.request.SystemNotifySendAO;
import com.fnk.app.system.biz.dal.entity.SystemNotifyMessageDO;
import com.fnk.app.system.biz.dal.entity.SystemNotifyTemplateDO;
import com.fnk.app.system.biz.service.messages.MessageJsonUtils;
import com.fnk.app.system.biz.service.messages.MessageTemplateRenderService;
import com.fnk.common.tools.lang.AssertUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * 站内信发送服务。
 *
 * @author Enigma
 */
@Service
@RequiredArgsConstructor
public class SystemNotifySendService {
    private final SystemNotifyTemplateService templateService;
    private final SystemNotifyMessageService messageService;
    private final MessageTemplateRenderService renderService;

    @Transactional(rollbackFor = Exception.class)
    public List<SystemNotifyMessageDO> send(SystemNotifySendAO req) {
        AssertUtils.isNull(req, "站内信发送请求不能为空");
        AssertUtils.isEmpty(req.getUserIds(), "接收用户不能为空");
        SystemNotifyTemplateDO template = templateService.getEnabledByCode(req.getTemplateCode());
        Map<String, String> params = MessageJsonUtils.normalizeStringMap(req.getParams());
        String content = renderService.render(template.getContent(), params);
        List<SystemNotifyMessageDO> messages = req.getUserIds().stream()
                .filter(userId -> userId != null && !userId.isBlank())
                .distinct()
                .map(userId -> buildMessage(userId, template, content, params))
                .toList();
        AssertUtils.isTrue(CollUtil.isEmpty(messages), "接收用户不能为空");
        return messageService.create(messages);
    }

    private SystemNotifyMessageDO buildMessage(String userId,
                                               SystemNotifyTemplateDO template,
                                               String content,
                                               Map<String, String> params) {
        SystemNotifyMessageDO message = new SystemNotifyMessageDO();
        message.setUserId(userId);
        message.setTemplateId(template.getId());
        message.setTemplateCode(template.getCode());
        message.setTemplateNickname(template.getNickname());
        message.setTemplateContent(content);
        message.setTemplateType(template.getTemplateType());
        message.setTemplateParams(MessageJsonUtils.write(params));
        message.setReadStatus(false);
        return message;
    }
}
