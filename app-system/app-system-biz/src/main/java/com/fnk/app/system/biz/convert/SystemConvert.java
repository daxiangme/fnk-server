package com.fnk.app.system.biz.convert;

import com.fnk.app.system.api.model.request.AdminUserCreateAO;
import com.fnk.app.system.api.model.request.MenuCreateAO;
import com.fnk.app.system.api.model.request.RoleCreateAO;
import com.fnk.app.system.api.model.response.AdminUserVO;
import com.fnk.app.system.api.model.response.RoleInfoVO;
import com.fnk.app.system.api.model.response.SystemMenuVO;
import com.fnk.app.system.biz.dal.entity.AdminUserDO;
import com.fnk.app.system.biz.dal.entity.RoleInfoDO;
import com.fnk.app.system.biz.dal.entity.SystemMenuDO;
import com.fnk.common.db.vo.PageVO;
import com.fnk.starter.web.enums.GenderType;
import org.springframework.beans.BeanUtils;

import java.util.Collections;
import java.util.List;

/**
 * 系统模块对象转换。
 *
 * @author Enigma
 */
public class SystemConvert {
    private SystemConvert() {
    }

    public static AdminUserDO toAdminUserDO(AdminUserCreateAO source) {
        AdminUserDO target = new AdminUserDO();
        BeanUtils.copyProperties(source, target);
        if (source.getSex() != null) {
            target.setSex(GenderType.valueOf(source.getSex()));
        }
        return target;
    }

    public static AdminUserVO toAdminUserVO(AdminUserDO source) {
        if (source == null) {
            return null;
        }
        AdminUserVO target = new AdminUserVO();
        BeanUtils.copyProperties(source, target);
        if (source.getSex() != null) {
            target.setSex(source.getSex().name());
        }
        return target;
    }

    public static RoleInfoDO toRoleInfoDO(RoleCreateAO source) {
        RoleInfoDO target = new RoleInfoDO();
        BeanUtils.copyProperties(source, target);
        return target;
    }

    public static RoleInfoVO toRoleInfoVO(RoleInfoDO source) {
        if (source == null) {
            return null;
        }
        RoleInfoVO target = new RoleInfoVO();
        BeanUtils.copyProperties(source, target);
        return target;
    }

    public static SystemMenuDO toSystemMenuDO(MenuCreateAO source) {
        SystemMenuDO target = new SystemMenuDO();
        BeanUtils.copyProperties(source, target);
        return target;
    }

    public static SystemMenuVO toSystemMenuVO(SystemMenuDO source) {
        if (source == null) {
            return null;
        }
        SystemMenuVO target = new SystemMenuVO();
        BeanUtils.copyProperties(source, target);
        return target;
    }

    public static List<SystemMenuVO> toSystemMenuVOList(List<SystemMenuDO> source) {
        if (source == null) {
            return Collections.emptyList();
        }
        return source.stream().map(SystemConvert::toSystemMenuVO).toList();
    }

    public static List<RoleInfoVO> toRoleInfoVOList(List<RoleInfoDO> source) {
        if (source == null) {
            return Collections.emptyList();
        }
        return source.stream().map(SystemConvert::toRoleInfoVO).toList();
    }

    public static PageVO<AdminUserVO> toAdminUserPage(PageVO<AdminUserDO> source) {
        PageVO<AdminUserVO> target = new PageVO<>();
        BeanUtils.copyProperties(source, target, "records");
        target.setRecords(source.getRecords().stream().map(SystemConvert::toAdminUserVO).toList());
        return target;
    }

    public static PageVO<RoleInfoVO> toRoleInfoPage(PageVO<RoleInfoDO> source) {
        PageVO<RoleInfoVO> target = new PageVO<>();
        BeanUtils.copyProperties(source, target, "records");
        target.setRecords(source.getRecords().stream().map(SystemConvert::toRoleInfoVO).toList());
        return target;
    }
}
