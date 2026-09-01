package com.fnk.app.system.api.model.response;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 邮件模板响应对象。
 *
 * @author Enigma
 */
@Data
public class SystemMailTemplateVO {
    private String id;
    private String accountId;
    private String name;
    private String code;
    private String fromName;
    private String title;
    private String content;
    private List<String> params;
    private Boolean status;
    private String remark;
    private Date createTime;
    private Date updateTime;
}
