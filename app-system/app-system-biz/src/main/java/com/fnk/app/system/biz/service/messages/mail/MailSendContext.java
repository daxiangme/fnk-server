package com.fnk.app.system.biz.service.messages.mail;

import com.fnk.app.system.biz.dal.entity.SystemMailAccountDO;

/**
 * 邮件发送上下文。
 *
 * @author Enigma
 */
public record MailSendContext(
        SystemMailAccountDO account,
        String fromName,
        String toMail,
        String title,
        String content
) {
}
