package com.fnk.app.system.api.model.response;

import lombok.Data;

import java.util.Date;

/**
 * 系统字典类型响应对象。
 *
 * @author Enigma
 */
@Data
public class SystemDictTypeVO {
    private String id;
    private String dictCode;
    private String dictName;
    private Boolean status;
    private String remark;
    private Date createTime;
    private Date updateTime;
}
