package com.fnk.app.system.api.model.query;

import com.fnk.common.db.dto.SplitPageDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 站内信消息查询对象。
 *
 * @author Enigma
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class SystemNotifyMessageQuery extends SplitPageDTO {
    private String userId;
    private String templateCode;
    private Boolean readStatus;
}
