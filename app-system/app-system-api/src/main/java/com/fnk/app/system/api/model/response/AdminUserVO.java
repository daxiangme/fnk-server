package com.fnk.app.system.api.model.response;

import lombok.Data;

import java.util.List;

/**
 * 管理员响应。
 *
 * @author Enigma
 */
@Data
public class AdminUserVO {
    private String id;
    private String phone;
    private String username;
    private String avatar;
    private String sex;
    private String loginIp;
    private String deptId;
    private Boolean status;
    private List<String> roleIdList;
    private List<String> roles;
    private List<String> permissions;
    private List<SystemMenuVO> menus;
}
