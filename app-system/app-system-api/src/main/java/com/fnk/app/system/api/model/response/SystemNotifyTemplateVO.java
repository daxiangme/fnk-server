package com.fnk.app.system.api.model.response;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 站内信模板响应对象。
 *
 * @author Enigma
 */
@Data
public class SystemNotifyTemplateVO {
    private String id;
    private String name;
    private String code;
    private String nickname;
    private String templateType;
    private String content;
    private List<String> params;
    private Boolean status;
    private String remark;
    private Date createTime;
    private Date updateTime;
}
