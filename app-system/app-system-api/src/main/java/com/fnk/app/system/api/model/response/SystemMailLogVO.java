package com.fnk.app.system.api.model.response;

import lombok.Data;

import java.util.Date;
import java.util.Map;

/**
 * 邮件发送日志响应对象。
 *
 * @author Enigma
 */
@Data
public class SystemMailLogVO {
    private String id;
    private String accountId;
    private String templateId;
    private String code;
    private String fromMail;
    private String toMail;
    private String title;
    private String content;
    private Map<String, String> templateParams;
    private String sendStatus;
    private Date sendTime;
    private String errorMsg;
    private Date createTime;
}
