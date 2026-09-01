package com.fnk.app.system.biz.service;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fnk.app.system.api.model.query.SystemNotifyMessageQuery;
import com.fnk.app.system.biz.dal.entity.SystemNotifyMessageDO;
import com.fnk.app.system.biz.dal.mapper.SystemNotifyMessageMapper;
import com.fnk.common.db.impl.BaseService;
import com.fnk.common.db.vo.PageVO;
import com.fnk.common.tools.lang.AssertUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * 站内信消息服务。
 *
 * @author Enigma
 */
@Service
public class SystemNotifyMessageService extends BaseService<SystemNotifyMessageMapper, SystemNotifyMessageDO> {

    public PageVO<SystemNotifyMessageDO> page(SystemNotifyMessageQuery query) {
        return this.basicPage(query, SystemNotifyMessageDO::getCreateTime, wrapper -> wrapper
                .eq(StrUtil.isNotBlank(query.getUserId()), SystemNotifyMessageDO::getUserId, query.getUserId())
                .eq(StrUtil.isNotBlank(query.getTemplateCode()), SystemNotifyMessageDO::getTemplateCode, query.getTemplateCode())
                .eq(query.getReadStatus() != null, SystemNotifyMessageDO::getReadStatus, query.getReadStatus()));
    }

    public PageVO<SystemNotifyMessageDO> pageMy(String userId, SystemNotifyMessageQuery query) {
        query.setUserId(userId);
        return page(query);
    }

    public List<SystemNotifyMessageDO> listUnread(String userId, int limit) {
        return this.list(new LambdaQueryWrapper<SystemNotifyMessageDO>()
                .eq(SystemNotifyMessageDO::getUserId, userId)
                .eq(SystemNotifyMessageDO::getReadStatus, false)
                .orderByDesc(SystemNotifyMessageDO::getCreateTime)
                .last("limit " + Math.max(1, Math.min(limit, 100))));
    }

    public long unreadCount(String userId) {
        return this.count(new LambdaQueryWrapper<SystemNotifyMessageDO>()
                .eq(SystemNotifyMessageDO::getUserId, userId)
                .eq(SystemNotifyMessageDO::getReadStatus, false));
    }

    @Transactional(rollbackFor = Exception.class)
    public void read(String userId, String id) {
        SystemNotifyMessageDO message = this.detail(id, wrapper -> wrapper.eq(SystemNotifyMessageDO::getUserId, userId));
        if (Boolean.TRUE.equals(message.getReadStatus())) {
            return;
        }
        message.setReadStatus(true);
        message.setReadTime(new Date());
        AssertUtils.isFalse(message.updateById(), "标记站内信已读失败");
    }

    @Transactional(rollbackFor = Exception.class)
    public void readAll(String userId) {
        SystemNotifyMessageDO update = new SystemNotifyMessageDO();
        update.setReadStatus(true);
        update.setReadTime(new Date());
        this.update(update, new LambdaQueryWrapper<SystemNotifyMessageDO>()
                .eq(SystemNotifyMessageDO::getUserId, userId)
                .eq(SystemNotifyMessageDO::getReadStatus, false));
    }
}
