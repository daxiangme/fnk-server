package com.fnk.app.system.biz.dal.mapper;

import com.fnk.app.system.biz.dal.entity.SystemMenuDO;
import com.fnk.app.system.biz.dal.entity.UserRoleDO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * 用户和角色关联 Mapper 接口
 * </p>
 *
 * @author Enigma
 * @since 2023-12-18
 */
@Mapper
public interface UserRoleMapper extends BaseMapper<UserRoleDO> {

    @Delete("delete from user_role where user_id = #{userId}")
    int deleteByUserId(String userId);

    @Select("select role_id from user_role where user_id = #{userId} and deleted = 0")
    List<String> queryRoleIdsByUserId(String userId);

    List<SystemMenuDO> queryMenusByUserId(String userId);

    List<String> queryRoleKeyByUserId(String userId);

    List<String> queryPermissionKeyByUserId(String userId);
}
