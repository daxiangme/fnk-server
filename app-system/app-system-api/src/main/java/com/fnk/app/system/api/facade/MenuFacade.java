package com.fnk.app.system.api.facade;

import com.fnk.app.system.api.model.query.MenuQuery;
import com.fnk.app.system.api.model.request.MenuCreateAO;
import com.fnk.app.system.api.model.request.MenuUpdateAO;
import com.fnk.app.system.api.model.response.SystemMenuVO;

import java.util.List;

/**
 * 菜单 facade。
 *
 * @author Enigma
 */
public interface MenuFacade {
    List<SystemMenuVO> list(MenuQuery query);

    List<SystemMenuVO> listByRootId(String rootId);

    SystemMenuVO detail(String id);

    SystemMenuVO create(MenuCreateAO req);

    SystemMenuVO update(String id, MenuUpdateAO req);

    void remove(String id);

    void remove(List<String> idList);
}
