package com.fnk.app.system.biz.service.messages.mail;

/**
 * 邮件发送适配器。
 *
 * @author Enigma
 */
public interface MailDeliveryClient {
    void send(MailSendContext context);
}
