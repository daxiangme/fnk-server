package com.fnk.app.system.biz.dal.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fnk.common.db.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 邮件发送日志。
 *
 * @author Enigma
 */
@Data
@TableName("system_mail_log")
@EqualsAndHashCode(callSuper = true)
public class SystemMailLogDO extends BaseEntity<SystemMailLogDO> {
    private String accountId;
    private String templateId;
    private String code;
    private String fromMail;
    private String toMail;
    private String title;
    private String content;
    private String templateParams;
    private String sendStatus;
    private Date sendTime;
    private String errorMsg;
}
