package com.fnk.app.system.biz.dal.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fnk.app.system.biz.dal.entity.SystemSmsLogDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 短信发送日志 Mapper。
 *
 * @author Enigma
 */
@Mapper
public interface SystemSmsLogMapper extends BaseMapper<SystemSmsLogDO> {
}
