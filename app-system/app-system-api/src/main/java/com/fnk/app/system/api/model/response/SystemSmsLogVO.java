package com.fnk.app.system.api.model.response;

import lombok.Data;

import java.util.Date;
import java.util.Map;

/**
 * 短信发送日志响应对象。
 *
 * @author Enigma
 */
@Data
public class SystemSmsLogVO {
    private String id;
    private String channelId;
    private String templateId;
    private String templateCode;
    private String mobile;
    private String content;
    private Map<String, String> templateParams;
    private String sendStatus;
    private Date sendTime;
    private String errorMsg;
    private Date createTime;
}
