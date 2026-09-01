package com.fnk.app.system.biz.service.messages.sms;

import org.springframework.stereotype.Component;

/**
 * DEBUG 短信适配器，用于本地和未接入云厂商前的闭环验证。
 *
 * @author Enigma
 */
@Component
public class DebugSmsSender implements SmsSender {
    @Override
    public String channelCode() {
        return "DEBUG";
    }

    @Override
    public SmsSendResult send(SmsSendContext context) {
        return new SmsSendResult(true, "DEBUG 短信已记录");
    }
}
