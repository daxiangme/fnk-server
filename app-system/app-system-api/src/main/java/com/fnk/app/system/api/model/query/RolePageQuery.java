package com.fnk.app.system.api.model.query;

import com.fnk.common.db.dto.SplitPageDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 角色分页查询。
 *
 * @author Enigma
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class RolePageQuery extends SplitPageDTO {
    private String roleName;
    private String roleKey;
    private Boolean status;
}
