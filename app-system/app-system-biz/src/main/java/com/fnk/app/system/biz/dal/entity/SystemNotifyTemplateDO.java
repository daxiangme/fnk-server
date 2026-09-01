package com.fnk.app.system.biz.dal.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fnk.common.db.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 站内信模板。
 *
 * @author Enigma
 */
@Data
@TableName("system_notify_template")
@EqualsAndHashCode(callSuper = true)
public class SystemNotifyTemplateDO extends BaseEntity<SystemNotifyTemplateDO> {
    private String name;
    private String code;
    private String nickname;
    private String templateType;
    private String content;
    private String params;
    private Boolean status;
    private String remark;
}
