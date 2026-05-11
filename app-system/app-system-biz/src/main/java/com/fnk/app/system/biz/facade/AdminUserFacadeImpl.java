package com.fnk.app.system.biz.facade;

import com.fnk.app.system.api.facade.AdminUserFacade;
import com.fnk.app.system.api.model.query.AdminUserPageQuery;
import com.fnk.app.system.api.model.request.AdminUserCreateAO;
import com.fnk.app.system.api.model.request.AdminUserUpdateAO;
import com.fnk.app.system.api.model.response.AdminUserVO;
import com.fnk.app.system.biz.convert.SystemConvert;
import com.fnk.app.system.biz.dal.entity.AdminUserDO;
import com.fnk.app.system.biz.service.AdminUserService;
import com.fnk.common.db.vo.PageVO;
import com.fnk.starter.web.enums.GenderType;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 系统用户 facade 实现。
 *
 * @author Enigma
 */
@Service
@AllArgsConstructor
public class AdminUserFacadeImpl implements AdminUserFacade {
    private final AdminUserService adminUserService;

    @Override
    public PageVO<AdminUserVO> page(AdminUserPageQuery query) {
        PageVO<AdminUserDO> page = adminUserService.basicPage(query, AdminUserDO::getCreateTime, wrapper -> {
            wrapper.like(query.getPhone() != null, AdminUserDO::getPhone, query.getPhone());
            wrapper.like(query.getUsername() != null, AdminUserDO::getUsername, query.getUsername());
            wrapper.like(query.getAvatar() != null, AdminUserDO::getAvatar, query.getAvatar());
            if (query.getSex() != null) {
                wrapper.eq(AdminUserDO::getSex, GenderType.valueOf(query.getSex()));
            }
            wrapper.like(query.getLoginIp() != null, AdminUserDO::getLoginIp, query.getLoginIp());
            wrapper.eq(query.getStatus() != null, AdminUserDO::getStatus, query.getStatus());
        });
        return SystemConvert.toAdminUserPage(page);
    }

    @Override
    public AdminUserVO detail(String id) {
        return SystemConvert.toAdminUserVO(adminUserService.queryDetail(id));
    }

    @Override
    public List<String> queryUserRoleIds(String id) {
        return adminUserService.queryUserRoleIds(id);
    }

    @Override
    public AdminUserVO create(AdminUserCreateAO req) {
        return SystemConvert.toAdminUserVO(adminUserService.saveAdminUser(SystemConvert.toAdminUserDO(req)));
    }

    @Override
    public AdminUserVO update(String id, AdminUserUpdateAO req) {
        AdminUserDO adminUser = SystemConvert.toAdminUserDO(req);
        adminUser.setId(id);
        return SystemConvert.toAdminUserVO(adminUserService.updateAdminUser(adminUser));
    }

    @Override
    public void remove(String id) {
        adminUserService.removeSingle(id);
    }

    @Override
    public void remove(List<String> idList) {
        adminUserService.remove(idList);
    }
}
