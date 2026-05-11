package com.fnk.app.system.api.model.response;

import com.fnk.app.system.api.enums.MenuTypeEnum;
import lombok.Data;

/**
 * 系统菜单响应。
 *
 * @author Enigma
 */
@Data
public class SystemMenuVO {
    private String id;
    private String rootId;
    private String name;
    private String routeKey;
    private Integer orderSort;
    private Boolean isIframe;
    private String path;
    private String icon;
    private String localIcon;
    private Boolean visible;
    private String permission;
    private MenuTypeEnum type;
    private String remark;
}
