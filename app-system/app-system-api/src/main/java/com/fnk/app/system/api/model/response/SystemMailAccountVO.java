package com.fnk.app.system.api.model.response;

import lombok.Data;

import java.util.Date;

/**
 * 邮箱账号响应对象。
 *
 * @author Enigma
 */
@Data
public class SystemMailAccountVO {
    private String id;
    private String mail;
    private String username;
    private String password;
    private String host;
    private Integer port;
    private Boolean sslEnable;
    private Boolean starttlsEnable;
    private Boolean status;
    private String remark;
    private Date createTime;
    private Date updateTime;
}
