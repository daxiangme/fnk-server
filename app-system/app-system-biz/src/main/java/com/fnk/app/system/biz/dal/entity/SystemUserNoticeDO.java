package com.fnk.app.system.biz.dal.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fnk.common.db.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 用户站内通知。
 *
 * @author Enigma
 */
@Data
@TableName("system_user_notice")
@EqualsAndHashCode(callSuper = true)
public class SystemUserNoticeDO extends BaseEntity<SystemUserNoticeDO> {
    private String userId;
    private String noticeId;
    private String title;
    private String noticeType;
    private String content;
    private Boolean readStatus;
    private Date readTime;
}
