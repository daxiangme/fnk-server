package com.fnk.app.system.biz.service;

import cn.hutool.core.util.StrUtil;
import com.fnk.app.system.api.model.request.SystemMailSendAO;
import com.fnk.app.system.biz.dal.entity.SystemMailAccountDO;
import com.fnk.app.system.biz.dal.entity.SystemMailLogDO;
import com.fnk.app.system.biz.dal.entity.SystemMailTemplateDO;
import com.fnk.app.system.biz.service.messages.MessageJsonUtils;
import com.fnk.app.system.biz.service.messages.MessageSendStatus;
import com.fnk.app.system.biz.service.messages.MessageTemplateRenderService;
import com.fnk.app.system.biz.service.messages.mail.MailDeliveryClient;
import com.fnk.app.system.biz.service.messages.mail.MailSendContext;
import com.fnk.common.tools.lang.AssertUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;

/**
 * 邮件发送服务。
 *
 * @author Enigma
 */
@Service
@RequiredArgsConstructor
public class SystemMailSendService {
    private final SystemMailTemplateService templateService;
    private final SystemMailAccountService accountService;
    private final SystemMailLogService logService;
    private final MessageTemplateRenderService renderService;
    private final MailDeliveryClient deliveryClient;

    public SystemMailLogDO send(SystemMailSendAO req) {
        AssertUtils.isNull(req, "邮件发送请求不能为空");
        AssertUtils.isBlank(req.getToMail(), "收件邮箱不能为空");
        SystemMailTemplateDO template = templateService.getEnabledByCode(req.getTemplateCode());
        SystemMailAccountDO account = accountService.detail(template.getAccountId());
        AssertUtils.isFalse(Boolean.TRUE.equals(account.getStatus()), "邮箱账号已停用");
        Map<String, String> params = MessageJsonUtils.normalizeStringMap(req.getParams());
        String title = renderService.render(template.getTitle(), params);
        String content = renderService.render(template.getContent(), params);

        SystemMailLogDO log = new SystemMailLogDO();
        log.setAccountId(account.getId());
        log.setTemplateId(template.getId());
        log.setCode(template.getCode());
        log.setFromMail(account.getMail());
        log.setToMail(req.getToMail());
        log.setTitle(title);
        log.setContent(content);
        log.setTemplateParams(MessageJsonUtils.write(params));
        log.setSendStatus(MessageSendStatus.INIT);
        log.setSendTime(new Date());
        log = logService.create(log);

        try {
            deliveryClient.send(new MailSendContext(account, template.getFromName(), req.getToMail(), title, content));
            log.setSendStatus(MessageSendStatus.SUCCESS);
        } catch (Exception ex) {
            log.setSendStatus(MessageSendStatus.FAILED);
            log.setErrorMsg(StrUtil.maxLength(ex.getMessage(), 500));
        }
        return updateLog(log);
    }

    private SystemMailLogDO updateLog(SystemMailLogDO log) {
        logService.update(log.getId(), log);
        return logService.detail(log.getId());
    }
}
