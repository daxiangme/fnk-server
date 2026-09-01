package com.fnk.app.system.api.model.query;

import com.fnk.common.db.dto.SplitPageDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 邮件模板查询对象。
 *
 * @author Enigma
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class SystemMailTemplateQuery extends SplitPageDTO {
    private String accountId;
    private String name;
    private String code;
    private Boolean status;
}
