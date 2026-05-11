package com.fnk.app.system.api.facade;

import com.fnk.app.system.api.model.query.RolePageQuery;
import com.fnk.app.system.api.model.request.RoleCreateAO;
import com.fnk.app.system.api.model.request.RoleUpdateAO;
import com.fnk.app.system.api.model.response.RoleInfoVO;
import com.fnk.common.db.vo.PageVO;

import java.util.List;

/**
 * 角色 facade。
 *
 * @author Enigma
 */
public interface RoleFacade {
    PageVO<RoleInfoVO> page(RolePageQuery query);

    List<RoleInfoVO> listAll();

    RoleInfoVO detail(String id);

    List<String> queryRoleMenuIds(String id);

    RoleInfoVO create(RoleCreateAO req);

    RoleInfoVO update(String id, RoleUpdateAO req);

    int remove(String id);

    void remove(List<String> idList);
}
