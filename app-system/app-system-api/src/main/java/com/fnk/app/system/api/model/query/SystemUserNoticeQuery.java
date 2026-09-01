package com.fnk.app.system.api.model.query;

import com.fnk.common.db.dto.SplitPageDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 用户站内通知查询。
 *
 * @author Enigma
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SystemUserNoticeQuery extends SplitPageDTO {
    @Schema(description = "标题")
    private String title;

    @Schema(description = "通知类型")
    private String noticeType;

    @Schema(description = "已读状态")
    private Boolean readStatus;
}
