package com.fnk.app.system.api.model.response;

import lombok.Data;

import java.util.Date;

/**
 * 短信渠道响应对象。
 *
 * @author Enigma
 */
@Data
public class SystemSmsChannelVO {
    private String id;
    private String channelName;
    private String channelCode;
    private String accessKey;
    private String accessSecret;
    private String signature;
    private String endpoint;
    private Boolean status;
    private String remark;
    private Date createTime;
    private Date updateTime;
}
