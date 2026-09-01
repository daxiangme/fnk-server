package com.fnk.app.infra.biz.dal.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fnk.app.infra.biz.dal.entity.InfraFileConfigDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 文件配置 Mapper。
 *
 * @author Enigma
 */
@Mapper
public interface InfraFileConfigMapper extends BaseMapper<InfraFileConfigDO> {
}
