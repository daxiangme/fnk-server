package com.fnk.app.system.api.model.query;

import com.fnk.common.db.dto.SplitPageDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 系统用户分页查询。
 *
 * @author Enigma
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class AdminUserPageQuery extends SplitPageDTO {
    private String phone;
    private String username;
    private String avatar;
    private String sex;
    private String loginIp;
    private Boolean status;
}
