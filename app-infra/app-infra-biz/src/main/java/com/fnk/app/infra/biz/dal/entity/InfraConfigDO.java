package com.fnk.app.infra.biz.dal.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fnk.common.db.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 系统参数配置。
 *
 * @author Enigma
 */
@Data
@TableName("infra_config")
@EqualsAndHashCode(callSuper = true)
public class InfraConfigDO extends BaseEntity<InfraConfigDO> {
    private String configName;
    private String configKey;
    private String configValue;
    private String groupCode;
    private String valueType;
    private Boolean visible;
    private Boolean status;
    private String remark;
}
