package com.fnk.app.system.api.model.request;

import lombok.Data;

import java.util.List;

/**
 * 创建角色请求。
 *
 * @author Enigma
 */
@Data
public class RoleCreateAO {
    private String roleName;
    private String roleKey;
    private Integer orderSort;
    private List<String> roleScope;
    private Boolean status;
}
