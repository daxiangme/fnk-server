package com.fnk.app.system.biz.service.messages.sms;

/**
 * 短信发送适配器。
 *
 * @author Enigma
 */
public interface SmsSender {
    String channelCode();

    SmsSendResult send(SmsSendContext context);
}
