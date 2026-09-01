package com.fnk.app.system.biz.facade;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fnk.app.system.api.facade.RoleFacade;
import com.fnk.app.system.api.model.query.RolePageQuery;
import com.fnk.app.system.api.model.request.RoleCreateAO;
import com.fnk.app.system.api.model.request.RoleUpdateAO;
import com.fnk.app.system.api.model.response.RoleInfoVO;
import com.fnk.app.system.biz.convert.SystemConvert;
import com.fnk.app.system.biz.dal.entity.RoleInfoDO;
import com.fnk.app.system.biz.service.RoleInfoService;
import com.fnk.common.db.vo.PageVO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 角色 facade 实现。
 *
 * @author Enigma
 */
@Service
@AllArgsConstructor
public class RoleFacadeImpl implements RoleFacade {
    private final RoleInfoService roleInfoService;

    @Override
    public PageVO<RoleInfoVO> page(RolePageQuery query) {
        PageVO<RoleInfoDO> page = roleInfoService.basicPage(query, RoleInfoDO::getRoleScope, wrapper -> {
            wrapper.like(query.getRoleName() != null, RoleInfoDO::getRoleName, query.getRoleName());
            wrapper.like(query.getRoleKey() != null, RoleInfoDO::getRoleKey, query.getRoleKey());
            wrapper.eq(query.getStatus() != null, RoleInfoDO::getStatus, query.getStatus());
        });
        return SystemConvert.toRoleInfoPage(page);
    }

    @Override
    public List<RoleInfoVO> listAll() {
        return SystemConvert.toRoleInfoVOList(roleInfoService.list(new LambdaQueryWrapper<RoleInfoDO>().eq(RoleInfoDO::getStatus, true)));
    }

    @Override
    public RoleInfoVO detail(String id) {
        return SystemConvert.toRoleInfoVO(roleInfoService.queryDetail(id));
    }

    @Override
    public List<String> queryRoleMenuIds(String id) {
        return roleInfoService.queryRoleMenuIds(id);
    }

    @Override
    public RoleInfoVO create(RoleCreateAO req) {
        return SystemConvert.toRoleInfoVO(roleInfoService.saveRole(SystemConvert.toRoleInfoDO(req)));
    }

    @Override
    public RoleInfoVO update(String id, RoleUpdateAO req) {
        RoleInfoDO roleInfo = SystemConvert.toRoleInfoDO(req);
        roleInfo.setId(id);
        return SystemConvert.toRoleInfoVO(roleInfoService.updateRole(roleInfo));
    }

    @Override
    public int remove(String id) {
        return roleInfoService.deleteByRoleId(id);
    }

    @Override
    public void remove(List<String> idList) {
        roleInfoService.deleteByRoleIds(idList);
    }
}
