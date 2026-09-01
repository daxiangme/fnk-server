package com.fnk.app.system.api.model.query;

import com.fnk.common.db.dto.SplitPageDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 邮箱账号查询对象。
 *
 * @author Enigma
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class SystemMailAccountQuery extends SplitPageDTO {
    private String mail;
    private String host;
    private Boolean status;
}
