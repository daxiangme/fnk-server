package com.fnk.app.system.biz.dal.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fnk.app.system.biz.dal.entity.SystemUserNoticeDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户站内通知 Mapper。
 *
 * @author Enigma
 */
@Mapper
public interface SystemUserNoticeMapper extends BaseMapper<SystemUserNoticeDO> {
}
