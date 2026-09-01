package com.fnk.app.system.api.model.query;

import com.fnk.common.db.dto.SplitPageDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 站内信模板查询对象。
 *
 * @author Enigma
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class SystemNotifyTemplateQuery extends SplitPageDTO {
    private String name;
    private String code;
    private Boolean status;
}
