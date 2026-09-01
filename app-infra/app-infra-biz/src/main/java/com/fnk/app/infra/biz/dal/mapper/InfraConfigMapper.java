package com.fnk.app.infra.biz.dal.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fnk.app.infra.biz.dal.entity.InfraConfigDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 系统参数配置 Mapper。
 *
 * @author Enigma
 */
@Mapper
public interface InfraConfigMapper extends BaseMapper<InfraConfigDO> {
}
