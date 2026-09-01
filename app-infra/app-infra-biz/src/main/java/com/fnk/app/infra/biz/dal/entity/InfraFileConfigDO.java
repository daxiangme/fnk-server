package com.fnk.app.infra.biz.dal.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fnk.common.db.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 文件存储配置。
 *
 * @author Enigma
 */
@Data
@TableName("infra_file_config")
@EqualsAndHashCode(callSuper = true)
public class InfraFileConfigDO extends BaseEntity<InfraFileConfigDO> {
    private String name;
    private String storageType;
    private Boolean master;
    private String basePath;
    private String domain;
    private Long maxSizeMb;
    private String endpoint;
    private String bucket;
    private String accessKey;
    private String accessSecret;
    private Boolean enablePathStyleAccess;
    private String remark;
}
