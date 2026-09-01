package com.fnk.app.system.api.model.query;

import com.fnk.common.db.dto.SplitPageDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 系统字典项查询对象。
 *
 * @author Enigma
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class SystemDictItemQuery extends SplitPageDTO {
    private String dictCode;
    private String label;
    private String value;
    private Boolean status;
}
