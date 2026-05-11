package com.fnk.app.system.api.model.response;

import lombok.Data;

import java.util.List;

/**
 * 角色响应。
 *
 * @author Enigma
 */
@Data
public class RoleInfoVO {
    private String id;
    private String roleName;
    private String roleKey;
    private Integer orderSort;
    private List<String> roleScope;
    private Boolean status;
}
