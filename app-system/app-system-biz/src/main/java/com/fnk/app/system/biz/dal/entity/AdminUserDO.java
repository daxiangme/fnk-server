package com.fnk.app.system.biz.dal.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fnk.common.db.entity.BaseEntity;
import com.fnk.starter.web.enums.GenderType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

/**
* 系统用户
*
* @author Enigma
* @since 2023-12-18
*/
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("admin_user")
@Schema(name = "AdminUserDO", description = "系统用户")
public class AdminUserDO extends BaseEntity<AdminUserDO> {



    /**
    * 手机号
    */
    @Schema(description="手机号")
    @NotBlank(message = "手机号码不能为空")
    private String phone;

    /**
    * 密码
    */
    @Schema(description="密码")
    private String password;

    /**
    * 盐
    */
    @Schema(description="盐")
    private String salt;

    /**
    * 用户名称
    */
    @Schema(description="用户名称")
    private String username;

    /**
    * 用户头像
    */
    @Schema(description="用户头像")
    private String avatar;

    /**
    * 用户性别
    */
    @Schema(description="用户性别")
    private GenderType sex;

    /**
    * 登录ip
    */
    @Schema(description="登录ip")
    private String loginIp;

    /**
    * 部门ID
    */
    @Schema(description="部门ID")
    private String deptId;

    /**
    * 用户状态;0正常 1不可用
    */
    @Schema(description="用户状态")
    private Boolean status;

    /**
     * 角色ID
     */
    @TableField(exist = false)
    @Size(min = 1,message = "必须分配角色给用户")
    private List<String> roleIdList;

}
