package com.fnk.app.system.biz.dal.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fnk.common.db.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 短信模板。
 *
 * @author Enigma
 */
@Data
@TableName("system_sms_template")
@EqualsAndHashCode(callSuper = true)
public class SystemSmsTemplateDO extends BaseEntity<SystemSmsTemplateDO> {
    private String channelId;
    private String templateName;
    private String templateCode;
    private String providerTemplateCode;
    private String templateType;
    private String content;
    private String params;
    private Boolean status;
    private String remark;
}
