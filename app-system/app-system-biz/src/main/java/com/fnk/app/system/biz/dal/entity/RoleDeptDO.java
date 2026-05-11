package com.fnk.app.system.biz.dal.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fnk.common.db.entity.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
* 角色关联部门
*
* @author Enigma
* @since 2023-12-18
*/
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("role_dept")
@Schema(name = "RoleDeptDO", description = "角色关联部门")
public class RoleDeptDO extends BaseEntity<RoleDeptDO> {

    /**
    * 角色ID
    */
    @Schema(description="角色ID")
    private String roleId;

    /**
    * 部门ID
    */
    @Schema(description="部门ID")
    private String deptId;


}
