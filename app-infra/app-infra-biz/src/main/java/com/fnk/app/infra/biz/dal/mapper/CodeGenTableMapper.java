package com.fnk.app.infra.biz.dal.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fnk.app.infra.biz.dal.entity.CodeGenTableDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 代码生成表配置 Mapper。
 *
 * @author Enigma
 */
@Mapper
public interface CodeGenTableMapper extends BaseMapper<CodeGenTableDO> {
}
