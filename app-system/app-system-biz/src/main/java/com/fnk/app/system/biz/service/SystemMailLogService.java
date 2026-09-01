package com.fnk.app.system.biz.service;

import cn.hutool.core.util.StrUtil;
import com.fnk.app.system.api.model.query.SystemMailLogQuery;
import com.fnk.app.system.biz.dal.entity.SystemMailLogDO;
import com.fnk.app.system.biz.dal.mapper.SystemMailLogMapper;
import com.fnk.common.db.impl.BaseService;
import com.fnk.common.db.vo.PageVO;
import org.springframework.stereotype.Service;

/**
 * 邮件发送日志服务。
 *
 * @author Enigma
 */
@Service
public class SystemMailLogService extends BaseService<SystemMailLogMapper, SystemMailLogDO> {

    public PageVO<SystemMailLogDO> page(SystemMailLogQuery query) {
        return this.basicPage(query, SystemMailLogDO::getCreateTime, wrapper -> wrapper
                .eq(StrUtil.isNotBlank(query.getAccountId()), SystemMailLogDO::getAccountId, query.getAccountId())
                .eq(StrUtil.isNotBlank(query.getTemplateId()), SystemMailLogDO::getTemplateId, query.getTemplateId())
                .eq(StrUtil.isNotBlank(query.getCode()), SystemMailLogDO::getCode, query.getCode())
                .like(StrUtil.isNotBlank(query.getToMail()), SystemMailLogDO::getToMail, query.getToMail())
                .eq(StrUtil.isNotBlank(query.getSendStatus()), SystemMailLogDO::getSendStatus, query.getSendStatus()));
    }
}
