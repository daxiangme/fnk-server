package com.fnk.app.system.biz.dal.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fnk.app.system.biz.dal.entity.SystemMailLogDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 邮件发送日志 Mapper。
 *
 * @author Enigma
 */
@Mapper
public interface SystemMailLogMapper extends BaseMapper<SystemMailLogDO> {
}
