package com.fnk.app.system.biz.dal.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fnk.common.db.entity.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
* 岗位信息
*
* @author Enigma
* @since 2023-12-18
*/
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("post_info")
@Schema(name = "PostInfoDO", description = "岗位信息")
public class PostInfoDO extends BaseEntity<PostInfoDO> {

    /**
    * 名称
    */
    @Schema(description="名称")
    private String name;

    /**
    * 岗位key
    */
    @Schema(description="岗位key")
    private String keyWord;

    /**
    * 排序
    */
    @Schema(description="排序")
    private Integer orderSort;

    /**
    * 状态
    */
    @Schema(description="状态")
    private Boolean status;


}
