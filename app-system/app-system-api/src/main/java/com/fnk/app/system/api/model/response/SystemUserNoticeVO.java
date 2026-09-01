package com.fnk.app.system.api.model.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

/**
 * 用户站内通知响应。
 *
 * @author Enigma
 */
@Data
public class SystemUserNoticeVO {
    @Schema(description = "ID")
    private String id;

    @Schema(description = "用户 ID")
    private String userId;

    @Schema(description = "通知公告 ID")
    private String noticeId;

    @Schema(description = "标题")
    private String title;

    @Schema(description = "通知类型")
    private String noticeType;

    @Schema(description = "内容")
    private String content;

    @Schema(description = "已读状态")
    private Boolean readStatus;

    @Schema(description = "阅读时间")
    private Date readTime;

    @Schema(description = "创建时间")
    private Date createTime;
}
