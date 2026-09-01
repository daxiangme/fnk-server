package com.fnk.app.system.api.model.query;

import com.fnk.common.db.dto.SplitPageDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 短信渠道查询对象。
 *
 * @author Enigma
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class SystemSmsChannelQuery extends SplitPageDTO {
    private String channelName;
    private String channelCode;
    private Boolean status;
}
