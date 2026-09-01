package com.fnk.app.system.api.model.query;

import com.fnk.common.db.dto.SplitPageDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 系统字典类型查询对象。
 *
 * @author Enigma
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class SystemDictTypeQuery extends SplitPageDTO {
    private String dictCode;
    private String dictName;
    private Boolean status;
}
