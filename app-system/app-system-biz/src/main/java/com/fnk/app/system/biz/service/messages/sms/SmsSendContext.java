package com.fnk.app.system.biz.service.messages.sms;

import com.fnk.app.system.biz.dal.entity.SystemSmsChannelDO;
import com.fnk.app.system.biz.dal.entity.SystemSmsTemplateDO;

import java.util.Map;

/**
 * 短信发送上下文。
 *
 * @author Enigma
 */
public record SmsSendContext(
        SystemSmsChannelDO channel,
        SystemSmsTemplateDO template,
        String mobile,
        String content,
        Map<String, String> params
) {
}
