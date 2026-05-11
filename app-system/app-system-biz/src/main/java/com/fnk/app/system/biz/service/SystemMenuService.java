package com.fnk.app.system.biz.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fnk.common.db.impl.BaseService;
import com.fnk.app.system.biz.dal.entity.SystemMenuDO;
import com.fnk.app.system.biz.dal.mapper.SystemMenuMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
* 系统菜单 服务实现层
*
* @author Enigma
* @since 2023-12-18
*/
@Service
@AllArgsConstructor
public class SystemMenuService extends BaseService<SystemMenuMapper, SystemMenuDO> {

    private RoleMenuService roleMenuService;
    public List<SystemMenuDO> listByRootId(String rootId) {
        return this.list(new LambdaQueryWrapper<SystemMenuDO>().eq(SystemMenuDO::getRootId, rootId));
    }

    @Transactional(rollbackFor = Exception.class)
    public int deleteMenu(String menuId) {
        int count = roleMenuService.deleteByMenuId(menuId);
        count += this.baseMapper.deleteMenu(menuId);
        return count;
    }
}
