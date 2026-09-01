package com.fnk.app.system.biz.dal.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fnk.common.db.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 邮箱账号。
 *
 * @author Enigma
 */
@Data
@TableName("system_mail_account")
@EqualsAndHashCode(callSuper = true)
public class SystemMailAccountDO extends BaseEntity<SystemMailAccountDO> {
    private String mail;
    private String username;
    private String password;
    private String host;
    private Integer port;
    private Boolean sslEnable;
    private Boolean starttlsEnable;
    private Boolean status;
    private String remark;
}
