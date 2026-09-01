package com.fnk.app.infra.biz.dal.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fnk.app.infra.biz.dal.entity.InfraFileDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 文件资源 Mapper。
 *
 * @author Enigma
 */
@Mapper
public interface InfraFileMapper extends BaseMapper<InfraFileDO> {
}
