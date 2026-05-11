package com.fnk.app.system.biz.dal.mapper;

import com.fnk.app.system.biz.dal.entity.SystemMenuDO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 系统菜单 Mapper 接口
 * </p>
 *
 * @author Enigma
 * @since 2023-12-18
 */
@Mapper
public interface SystemMenuMapper extends BaseMapper<SystemMenuDO> {

    @Delete("delete from system_menu where id = #{id}")
    int deleteMenu(String id);
}
