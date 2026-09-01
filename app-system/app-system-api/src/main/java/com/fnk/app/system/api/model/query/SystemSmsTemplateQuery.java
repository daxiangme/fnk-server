package com.fnk.app.system.api.model.query;

import com.fnk.common.db.dto.SplitPageDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 短信模板查询对象。
 *
 * @author Enigma
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class SystemSmsTemplateQuery extends SplitPageDTO {
    private String channelId;
    private String templateName;
    private String templateCode;
    private Boolean status;
}
