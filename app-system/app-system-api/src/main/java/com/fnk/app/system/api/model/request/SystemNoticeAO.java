package com.fnk.app.system.api.model.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 通知公告请求对象。
 *
 * @author Enigma
 */
@Data
public class SystemNoticeAO {
    @NotBlank(message = "通知标题不能为空")
    private String title;

    @NotBlank(message = "通知类型不能为空")
    private String noticeType;

    private String content;
    private Boolean publishStatus = false;
}
