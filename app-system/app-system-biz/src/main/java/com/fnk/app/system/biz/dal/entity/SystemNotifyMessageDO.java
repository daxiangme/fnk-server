package com.fnk.app.system.biz.dal.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fnk.common.db.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 站内信消息。
 *
 * @author Enigma
 */
@Data
@TableName("system_notify_message")
@EqualsAndHashCode(callSuper = true)
public class SystemNotifyMessageDO extends BaseEntity<SystemNotifyMessageDO> {
    private String userId;
    private String templateId;
    private String templateCode;
    private String templateNickname;
    private String templateContent;
    private String templateType;
    private String templateParams;
    private Boolean readStatus;
    private Date readTime;
}
