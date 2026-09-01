package com.fnk.app.system.biz.dal.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fnk.app.system.biz.dal.entity.SystemSmsTemplateDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 短信模板 Mapper。
 *
 * @author Enigma
 */
@Mapper
public interface SystemSmsTemplateMapper extends BaseMapper<SystemSmsTemplateDO> {
}
