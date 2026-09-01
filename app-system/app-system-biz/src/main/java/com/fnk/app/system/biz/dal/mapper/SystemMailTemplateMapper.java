package com.fnk.app.system.biz.dal.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fnk.app.system.biz.dal.entity.SystemMailTemplateDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 邮件模板 Mapper。
 *
 * @author Enigma
 */
@Mapper
public interface SystemMailTemplateMapper extends BaseMapper<SystemMailTemplateDO> {
}
