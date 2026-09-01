package com.fnk.app.system.api.model.response;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 短信模板响应对象。
 *
 * @author Enigma
 */
@Data
public class SystemSmsTemplateVO {
    private String id;
    private String channelId;
    private String templateName;
    private String templateCode;
    private String providerTemplateCode;
    private String templateType;
    private String content;
    private List<String> params;
    private Boolean status;
    private String remark;
    private Date createTime;
    private Date updateTime;
}
