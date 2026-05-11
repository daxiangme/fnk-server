package com.fnk.app.system.api.model.request;

import com.fnk.app.system.api.enums.MenuTypeEnum;
import lombok.Data;

/**
 * 创建菜单请求。
 *
 * @author Enigma
 */
@Data
public class MenuCreateAO {
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
