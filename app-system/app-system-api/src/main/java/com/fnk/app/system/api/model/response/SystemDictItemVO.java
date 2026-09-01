package com.fnk.app.system.api.model.response;

import lombok.Data;

import java.util.Date;

/**
 * 系统字典项响应对象。
 *
 * @author Enigma
 */
@Data
public class SystemDictItemVO {
    private String id;
    private String dictCode;
    private String label;
    private String value;
    private Integer orderSort;
    private Boolean status;
    private String tagType;
    private String remark;
    private Date createTime;
    private Date updateTime;
}
