package com.fnk.common.db.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * @author Enigma
 */
@Data
@Schema(name = "SplitPageDTO", description = "分页查询条件")
public class SplitPageDTO implements Serializable {
    /**
     * 当前页码
     */
    @Schema(name = "page", description = "当前页码")
    private int page = 1;

    /**
     * 每页数量大小
     */
    @Schema(name = "pageSize", description = "每页数量大小")
    private int pageSize = 10;

    /**
     * 正序查询
     */
    @Schema(name = "asc", description = "正序查询")
    private boolean asc = false;

    /**
     * 查询条件-ID
     */
    @Schema(name = "id", description = "查询条件-ID")
    private Long id;

    /**
     * 查询条件-创建时间
     */
    @Schema(name = "createTime", description = "查询条件-创建时间")
    private String createTime;

    /**
     * 查询条件-更新时间
     */
    @Schema(name = "updateTime", description = "查询条件-更新时间")
    private String updateTime;
}
