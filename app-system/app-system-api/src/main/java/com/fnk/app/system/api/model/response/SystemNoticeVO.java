package com.fnk.app.system.api.model.response;

import lombok.Data;

import java.util.Date;

/**
 * 通知公告响应对象。
 *
 * @author Enigma
 */
@Data
public class SystemNoticeVO {
    private String id;
    private String title;
    private String noticeType;
    private String content;
    private Boolean publishStatus;
    private Date publishTime;
    private Date createTime;
    private Date updateTime;
}
