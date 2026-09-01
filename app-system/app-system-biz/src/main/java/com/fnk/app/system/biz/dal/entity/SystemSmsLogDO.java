package com.fnk.app.system.biz.dal.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fnk.common.db.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 短信发送日志。
 *
 * @author Enigma
 */
@Data
@TableName("system_sms_log")
@EqualsAndHashCode(callSuper = true)
public class SystemSmsLogDO extends BaseEntity<SystemSmsLogDO> {
    private String channelId;
    private String templateId;
    private String templateCode;
    private String mobile;
    private String content;
    private String templateParams;
    private String sendStatus;
    private Date sendTime;
    private String errorMsg;
}
