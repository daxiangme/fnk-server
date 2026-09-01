package com.fnk.app.system.biz.dal.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fnk.common.db.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 短信渠道。
 *
 * @author Enigma
 */
@Data
@TableName("system_sms_channel")
@EqualsAndHashCode(callSuper = true)
public class SystemSmsChannelDO extends BaseEntity<SystemSmsChannelDO> {
    private String channelName;
    private String channelCode;
    private String accessKey;
    private String accessSecret;
    private String signature;
    private String endpoint;
    private Boolean status;
    private String remark;
}
