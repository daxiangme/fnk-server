package com.fnk.app.system.biz.dal.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fnk.app.system.biz.dal.entity.SystemMailAccountDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 邮箱账号 Mapper。
 *
 * @author Enigma
 */
@Mapper
public interface SystemMailAccountMapper extends BaseMapper<SystemMailAccountDO> {
}
