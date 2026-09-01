package com.fnk.app.infra.biz.dal.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fnk.common.db.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 文件资源。
 *
 * @author Enigma
 */
@Data
@TableName("infra_file")
@EqualsAndHashCode(callSuper = true)
public class InfraFileDO extends BaseEntity<InfraFileDO> {
    private String originalName;
    private String fileName;
    private String contentType;
    private Long fileSize;
    private String storageType;
    private String configId;
    private String configName;
    private String storagePath;
    private String url;
}
