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
 * 系统字典项。
 *
 * @author Enigma
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("system_dict_item")
@Schema(name = "SystemDictItemDO", description = "系统字典项")
public class SystemDictItemDO extends BaseEntity<SystemDictItemDO> {
    @TableField("dict_code")
    private String dictCode;

    private String label;

    private String value;

    @TableField("order_sort")
    private Integer orderSort;

    private Boolean status;

    @TableField("tag_type")
    private String tagType;

    private String remark;
}
