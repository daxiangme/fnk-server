package com.fnk.app.system.biz.dal.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fnk.common.db.entity.BaseEntity;
import com.fnk.app.system.api.enums.MenuTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
* 系统菜单
*
* @author Enigma
* @since 2023-12-18
*/
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("system_menu")
@Schema(name = "SystemMenuDO", description = "系统菜单")
public class SystemMenuDO extends BaseEntity<SystemMenuDO> {

    /**
     * 上级ID
     */
    @Schema(description="上级ID")
    private String rootId;

    /**
     * 菜单名称
     */
    @Schema(description="菜单名称")
    private String name;

    /**
     * 路由key全局唯一
     */
    @Schema(description="路由key全局唯一")
    private String routeKey;

    /**
     * 显示顺序
     */
    @Schema(description="显示顺序")
    private Integer orderSort;

    /**
     * 是否为网页
     */
    @Schema(description="是否为网页")
    private Boolean isIframe;

    /**
     * 请求路径
     */
    @Schema(description="请求路径")
    private String path;

    /**
     * icones
     */
    @Schema(description="icones")
    private String icon;

    /**
     * 本地icon
     */
    @Schema(description="本地icon")
    private String localIcon;

    /**
     * 是否显示
     */
    @Schema(description="是否显示")
    private Boolean visible;

    /**
     * 权限标识
     */
    @Schema(description="权限标识")
    private String permission;

    /**
     * 菜单类型（table目录 menu菜单 button按钮）
     */
    @Schema(description="菜单类型（table目录 menu菜单 button按钮）")
    private MenuTypeEnum type;

    /**
     * 备注
     */
    @Schema(description="备注")
    private String remark;


}
