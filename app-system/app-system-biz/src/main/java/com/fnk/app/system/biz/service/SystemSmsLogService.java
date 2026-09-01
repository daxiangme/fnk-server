package com.fnk.app.system.biz.service;

import cn.hutool.core.util.StrUtil;
import com.fnk.app.system.api.model.query.SystemSmsLogQuery;
import com.fnk.app.system.biz.dal.entity.SystemSmsLogDO;
import com.fnk.app.system.biz.dal.mapper.SystemSmsLogMapper;
import com.fnk.common.db.impl.BaseService;
import com.fnk.common.db.vo.PageVO;
import org.springframework.stereotype.Service;

/**
 * 短信发送日志服务。
 *
 * @author Enigma
 */
@Service
public class SystemSmsLogService extends BaseService<SystemSmsLogMapper, SystemSmsLogDO> {

    public PageVO<SystemSmsLogDO> page(SystemSmsLogQuery query) {
        return this.basicPage(query, SystemSmsLogDO::getCreateTime, wrapper -> wrapper
                .eq(StrUtil.isNotBlank(query.getChannelId()), SystemSmsLogDO::getChannelId, query.getChannelId())
                .eq(StrUtil.isNotBlank(query.getTemplateId()), SystemSmsLogDO::getTemplateId, query.getTemplateId())
                .eq(StrUtil.isNotBlank(query.getTemplateCode()), SystemSmsLogDO::getTemplateCode, query.getTemplateCode())
                .like(StrUtil.isNotBlank(query.getMobile()), SystemSmsLogDO::getMobile, query.getMobile())
                .eq(StrUtil.isNotBlank(query.getSendStatus()), SystemSmsLogDO::getSendStatus, query.getSendStatus()));
    }
}
