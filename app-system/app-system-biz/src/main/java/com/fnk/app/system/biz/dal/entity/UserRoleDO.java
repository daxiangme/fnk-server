package com.fnk.app.system.biz.dal.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fnk.common.db.entity.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
* 用户和角色关联
*
* @author Enigma
* @since 2023-12-18
*/
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("user_role")
@Schema(name = "UserRoleDO", description = "用户和角色关联")
public class UserRoleDO extends BaseEntity<UserRoleDO> {

    /**
    * 系统用户ID
    */
    @Schema(description="系统用户ID")
    private String userId;

    /**
    * 角色ID
    */
    @Schema(description="角色ID")
    private String roleId;


}
