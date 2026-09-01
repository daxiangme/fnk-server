package com.fnk.app.system.api.model.query;

import com.fnk.common.db.dto.SplitPageDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 邮件发送日志查询对象。
 *
 * @author Enigma
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class SystemMailLogQuery extends SplitPageDTO {
    private String accountId;
    private String templateId;
    private String code;
    private String toMail;
    private String sendStatus;
}
