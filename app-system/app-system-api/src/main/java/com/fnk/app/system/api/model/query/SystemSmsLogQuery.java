package com.fnk.app.system.api.model.query;

import com.fnk.common.db.dto.SplitPageDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 短信发送日志查询对象。
 *
 * @author Enigma
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class SystemSmsLogQuery extends SplitPageDTO {
    private String channelId;
    private String templateId;
    private String templateCode;
    private String mobile;
    private String sendStatus;
}
