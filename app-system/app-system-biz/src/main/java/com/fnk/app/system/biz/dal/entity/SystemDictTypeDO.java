package com.fnk.app.system.biz.dal.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fnk.common.db.entity.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 系统字典类型。
 *
 * @author Enigma
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("system_dict_type")
@Schema(name = "SystemDictTypeDO", description = "系统字典类型")
public class SystemDictTypeDO extends BaseEntity<SystemDictTypeDO> {
    @TableField("dict_code")
    private String dictCode;

    @TableField("dict_name")
    private String dictName;

    private Boolean status;

    private String remark;
}
