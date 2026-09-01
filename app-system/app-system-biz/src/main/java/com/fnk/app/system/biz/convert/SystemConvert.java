package com.fnk.app.system.biz.convert;

import com.fnk.app.system.api.model.request.AdminUserCreateAO;
import com.fnk.app.system.api.model.request.MenuCreateAO;
import com.fnk.app.system.api.model.request.RoleCreateAO;
import com.fnk.app.system.api.model.request.SystemDictItemAO;
import com.fnk.app.system.api.model.request.SystemDictTypeAO;
import com.fnk.app.system.api.model.request.SystemNoticeAO;
import com.fnk.app.system.api.model.response.AdminUserVO;
import com.fnk.app.system.api.model.response.RoleInfoVO;
import com.fnk.app.system.api.model.response.SystemDictItemVO;
import com.fnk.app.system.api.model.response.SystemDictTypeVO;
import com.fnk.app.system.api.model.response.SystemMenuVO;
import com.fnk.app.system.api.model.response.SystemNoticeVO;
import com.fnk.app.system.api.model.response.SystemUserNoticeVO;
import com.fnk.app.system.biz.dal.entity.AdminUserDO;
import com.fnk.app.system.biz.dal.entity.RoleInfoDO;
import com.fnk.app.system.biz.dal.entity.SystemDictItemDO;
import com.fnk.app.system.biz.dal.entity.SystemDictTypeDO;
import com.fnk.app.system.biz.dal.entity.SystemMenuDO;
import com.fnk.app.system.biz.dal.entity.SystemNoticeDO;
import com.fnk.app.system.biz.dal.entity.SystemUserNoticeDO;
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

    public static SystemDictTypeDO toSystemDictTypeDO(SystemDictTypeAO source) {
        SystemDictTypeDO target = new SystemDictTypeDO();
        BeanUtils.copyProperties(source, target);
        return target;
    }

    public static SystemDictTypeVO toSystemDictTypeVO(SystemDictTypeDO source) {
        if (source == null) {
            return null;
        }
        SystemDictTypeVO target = new SystemDictTypeVO();
        BeanUtils.copyProperties(source, target);
        return target;
    }

    public static SystemDictItemDO toSystemDictItemDO(SystemDictItemAO source) {
        SystemDictItemDO target = new SystemDictItemDO();
        BeanUtils.copyProperties(source, target);
        return target;
    }

    public static SystemDictItemVO toSystemDictItemVO(SystemDictItemDO source) {
        if (source == null) {
            return null;
        }
        SystemDictItemVO target = new SystemDictItemVO();
        BeanUtils.copyProperties(source, target);
        return target;
    }

    public static SystemNoticeDO toSystemNoticeDO(SystemNoticeAO source) {
        SystemNoticeDO target = new SystemNoticeDO();
        BeanUtils.copyProperties(source, target);
        return target;
    }

    public static SystemNoticeVO toSystemNoticeVO(SystemNoticeDO source) {
        if (source == null) {
            return null;
        }
        SystemNoticeVO target = new SystemNoticeVO();
        BeanUtils.copyProperties(source, target);
        return target;
    }

    public static SystemUserNoticeVO toSystemUserNoticeVO(SystemUserNoticeDO source) {
        if (source == null) {
            return null;
        }
        SystemUserNoticeVO target = new SystemUserNoticeVO();
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

    public static List<SystemDictTypeVO> toSystemDictTypeVOList(List<SystemDictTypeDO> source) {
        if (source == null) {
            return Collections.emptyList();
        }
        return source.stream().map(SystemConvert::toSystemDictTypeVO).toList();
    }

    public static List<SystemDictItemVO> toSystemDictItemVOList(List<SystemDictItemDO> source) {
        if (source == null) {
            return Collections.emptyList();
        }
        return source.stream().map(SystemConvert::toSystemDictItemVO).toList();
    }

    public static List<SystemNoticeVO> toSystemNoticeVOList(List<SystemNoticeDO> source) {
        if (source == null) {
            return Collections.emptyList();
        }
        return source.stream().map(SystemConvert::toSystemNoticeVO).toList();
    }

    public static List<SystemUserNoticeVO> toSystemUserNoticeVOList(List<SystemUserNoticeDO> source) {
        if (source == null) {
            return Collections.emptyList();
        }
        return source.stream().map(SystemConvert::toSystemUserNoticeVO).toList();
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

    public static PageVO<SystemDictTypeVO> toSystemDictTypePage(PageVO<SystemDictTypeDO> source) {
        PageVO<SystemDictTypeVO> target = new PageVO<>();
        BeanUtils.copyProperties(source, target, "records");
        target.setRecords(source.getRecords().stream().map(SystemConvert::toSystemDictTypeVO).toList());
        return target;
    }

    public static PageVO<SystemDictItemVO> toSystemDictItemPage(PageVO<SystemDictItemDO> source) {
        PageVO<SystemDictItemVO> target = new PageVO<>();
        BeanUtils.copyProperties(source, target, "records");
        target.setRecords(source.getRecords().stream().map(SystemConvert::toSystemDictItemVO).toList());
        return target;
    }

    public static PageVO<SystemNoticeVO> toSystemNoticePage(PageVO<SystemNoticeDO> source) {
        PageVO<SystemNoticeVO> target = new PageVO<>();
        BeanUtils.copyProperties(source, target, "records");
        target.setRecords(source.getRecords().stream().map(SystemConvert::toSystemNoticeVO).toList());
        return target;
    }

    public static PageVO<SystemUserNoticeVO> toSystemUserNoticePage(PageVO<SystemUserNoticeDO> source) {
        PageVO<SystemUserNoticeVO> target = new PageVO<>();
        BeanUtils.copyProperties(source, target, "records");
        target.setRecords(source.getRecords().stream().map(SystemConvert::toSystemUserNoticeVO).toList());
        return target;
    }
}
