package com.fnk.app.system.biz.service;

import cn.hutool.core.util.StrUtil;
import com.fnk.app.system.api.model.request.SystemSmsSendAO;
import com.fnk.app.system.biz.dal.entity.SystemSmsChannelDO;
import com.fnk.app.system.biz.dal.entity.SystemSmsLogDO;
import com.fnk.app.system.biz.dal.entity.SystemSmsTemplateDO;
import com.fnk.app.system.biz.service.messages.MessageJsonUtils;
import com.fnk.app.system.biz.service.messages.MessageSendStatus;
import com.fnk.app.system.biz.service.messages.MessageTemplateRenderService;
import com.fnk.app.system.biz.service.messages.sms.SmsSendContext;
import com.fnk.app.system.biz.service.messages.sms.SmsSendResult;
import com.fnk.app.system.biz.service.messages.sms.SmsSender;
import com.fnk.common.tools.lang.AssertUtils;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 短信发送服务。
 *
 * @author Enigma
 */
@Service
public class SystemSmsSendService {
    private final SystemSmsTemplateService templateService;
    private final SystemSmsChannelService channelService;
    private final SystemSmsLogService logService;
    private final MessageTemplateRenderService renderService;
    private final Map<String, SmsSender> senderMap;

    public SystemSmsSendService(SystemSmsTemplateService templateService,
                                SystemSmsChannelService channelService,
                                SystemSmsLogService logService,
                                MessageTemplateRenderService renderService,
                                List<SmsSender> senders) {
        this.templateService = templateService;
        this.channelService = channelService;
        this.logService = logService;
        this.renderService = renderService;
        this.senderMap = senders.stream().collect(Collectors.toMap(
                sender -> sender.channelCode().toUpperCase(Locale.ROOT),
                Function.identity(),
                (left, right) -> left
        ));
    }

    public SystemSmsLogDO send(SystemSmsSendAO req) {
        AssertUtils.isNull(req, "短信发送请求不能为空");
        AssertUtils.isBlank(req.getMobile(), "手机号不能为空");
        SystemSmsTemplateDO template = templateService.getEnabledByCode(req.getTemplateCode());
        SystemSmsChannelDO channel = channelService.detail(template.getChannelId());
        AssertUtils.isFalse(Boolean.TRUE.equals(channel.getStatus()), "短信渠道已停用");
        Map<String, String> params = MessageJsonUtils.normalizeStringMap(req.getParams());
        String content = renderService.render(template.getContent(), params);

        SystemSmsLogDO log = new SystemSmsLogDO();
        log.setChannelId(channel.getId());
        log.setTemplateId(template.getId());
        log.setTemplateCode(template.getTemplateCode());
        log.setMobile(req.getMobile());
        log.setContent(content);
        log.setTemplateParams(MessageJsonUtils.write(params));
        log.setSendStatus(MessageSendStatus.INIT);
        log.setSendTime(new Date());
        log = logService.create(log);

        try {
            SmsSender sender = senderMap.get(channel.getChannelCode().toUpperCase(Locale.ROOT));
            if (sender == null) {
                return markFailed(log, "短信渠道 " + channel.getChannelCode() + " 暂未配置发送适配器");
            }
            SmsSendResult result = sender.send(new SmsSendContext(channel, template, req.getMobile(), content, params));
            if (result.success()) {
                log.setSendStatus(MessageSendStatus.SUCCESS);
                log.setErrorMsg(StrUtil.blankToDefault(result.message(), null));
            } else {
                log.setSendStatus(MessageSendStatus.FAILED);
                log.setErrorMsg(StrUtil.blankToDefault(result.message(), "短信发送失败"));
            }
        } catch (Exception ex) {
            log.setSendStatus(MessageSendStatus.FAILED);
            log.setErrorMsg(StrUtil.maxLength(ex.getMessage(), 500));
        }
        return updateLog(log);
    }

    private SystemSmsLogDO markFailed(SystemSmsLogDO log, String message) {
        log.setSendStatus(MessageSendStatus.FAILED);
        log.setErrorMsg(message);
        return updateLog(log);
    }

    private SystemSmsLogDO updateLog(SystemSmsLogDO log) {
        logService.update(log.getId(), log);
        return logService.detail(log.getId());
    }
}
