package com.fnk.app.system.api.facade;

import com.fnk.app.system.api.model.query.AdminUserPageQuery;
import com.fnk.app.system.api.model.request.AdminUserCreateAO;
import com.fnk.app.system.api.model.request.AdminUserUpdateAO;
import com.fnk.app.system.api.model.response.AdminUserVO;
import com.fnk.common.db.vo.PageVO;

import java.util.List;

/**
 * 系统用户 facade。
 *
 * @author Enigma
 */
public interface AdminUserFacade {
    PageVO<AdminUserVO> page(AdminUserPageQuery query);

    AdminUserVO detail(String id);

    List<String> queryUserRoleIds(String id);

    AdminUserVO create(AdminUserCreateAO req);

    AdminUserVO update(String id, AdminUserUpdateAO req);

    void remove(String id);

    void remove(List<String> idList);
}
