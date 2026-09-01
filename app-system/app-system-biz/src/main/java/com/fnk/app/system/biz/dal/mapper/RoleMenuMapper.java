package com.fnk.app.system.biz.dal.mapper;

import com.fnk.app.system.biz.dal.entity.RoleMenuDO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * 角色和菜单关联 Mapper 接口
 * </p>
 *
 * @author Enigma
 * @since 2023-12-18
 */
@Mapper
public interface RoleMenuMapper extends BaseMapper<RoleMenuDO> {

    @Delete("delete from role_menu where menu_id = #{menuId}")
    int deleteByMenuId(String menuId);


    @Delete("delete from role_menu where role_id = #{roleId}")
    int deleteByRoleId(String roleId);

    @Select("""
            select srm.menu_id
            from role_menu srm
            inner join system_menu sm on sm.id = srm.menu_id
            where srm.role_id = #{roleId}
            and srm.deleted = 0
            and sm.deleted = 0
            """)
    List<String> queryMenuIdByRoleId(String roleId);

    /**
     * 查询角色所有权限key
     * @param roleId 角色id
     * @return 权限key
     */
    List<String> queryPermissionKeyByRoleId(String roleId);

    List<String> queryPermissionKeyByRoleKey(String roleKey);
}
