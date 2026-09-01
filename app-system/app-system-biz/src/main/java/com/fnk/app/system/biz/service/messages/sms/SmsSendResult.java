package com.fnk.app.system.biz.service.messages.sms;

/**
 * 短信发送结果。
 *
 * @author Enigma
 */
public record SmsSendResult(boolean success, String message) {
}
