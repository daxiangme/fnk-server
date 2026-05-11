package com.fnk.app.system.api.model.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

/**
 * 创建系统用户请求。
 *
 * @author Enigma
 */
@Data
public class AdminUserCreateAO {
    @NotBlank(message = "手机号码不能为空")
    private String phone;
    private String password;
    private String username;
    private String avatar;
    private String sex;
    private String deptId;
    private Boolean status;
    @Size(min = 1, message = "必须分配角色给用户")
    private List<String> roleIdList;
}
