package com.fnk.app.system.api.model.response;

import lombok.Data;

import java.util.Date;
import java.util.Map;

/**
 * 站内信消息响应对象。
 *
 * @author Enigma
 */
@Data
public class SystemNotifyMessageVO {
    private String id;
    private String userId;
    private String templateId;
    private String templateCode;
    private String templateNickname;
    private String templateContent;
    private String templateType;
    private Map<String, String> templateParams;
    private Boolean readStatus;
    private Date readTime;
    private Date createTime;
    private Date updateTime;
}
