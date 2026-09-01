package com.fnk.app.system.biz.service;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fnk.app.system.api.model.query.SystemNoticeQuery;
import com.fnk.app.system.biz.dal.entity.SystemNoticeDO;
import com.fnk.app.system.biz.dal.mapper.SystemNoticeMapper;
import com.fnk.common.db.impl.BaseService;
import com.fnk.common.db.vo.PageVO;
import com.fnk.common.tools.lang.AssertUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * 通知公告服务。
 *
 * @author Enigma
 */
@Service
@RequiredArgsConstructor
public class SystemNoticeService extends BaseService<SystemNoticeMapper, SystemNoticeDO> {
    private final SystemUserNoticeService userNoticeService;

    public PageVO<SystemNoticeDO> page(SystemNoticeQuery query) {
        return this.basicPage(query, SystemNoticeDO::getCreateTime, wrapper -> wrapper
                .like(StrUtil.isNotBlank(query.getTitle()), SystemNoticeDO::getTitle, query.getTitle())
                .eq(StrUtil.isNotBlank(query.getNoticeType()), SystemNoticeDO::getNoticeType, query.getNoticeType())
                .eq(query.getPublishStatus() != null, SystemNoticeDO::getPublishStatus, query.getPublishStatus()));
    }

    public List<SystemNoticeDO> listPublished() {
        return this.list(new LambdaQueryWrapper<SystemNoticeDO>()
                .eq(SystemNoticeDO::getPublishStatus, true)
                .orderByDesc(SystemNoticeDO::getPublishTime));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public SystemNoticeDO create(SystemNoticeDO req) {
        normalizeAndValidate(req);
        SystemNoticeDO notice = super.create(req);
        if (Boolean.TRUE.equals(notice.getPublishStatus())) {
            userNoticeService.deliver(notice);
        }
        return notice;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public SystemNoticeDO update(String id, SystemNoticeDO req) {
        req.setId(id);
        normalizeAndValidate(req);
        SystemNoticeDO notice = super.update(id, req);
        if (Boolean.TRUE.equals(notice.getPublishStatus())) {
            userNoticeService.deliver(notice);
        } else {
            userNoticeService.removeByNoticeId(notice.getId());
        }
        return notice;
    }

    @Transactional(rollbackFor = Exception.class)
    public SystemNoticeDO publish(String id) {
        SystemNoticeDO notice = this.detail(id);
        notice.setPublishStatus(true);
        notice.setPublishTime(new Date());
        AssertUtils.isFalse(notice.updateById(), "发布通知失败");
        userNoticeService.deliver(notice);
        return notice;
    }

    @Transactional(rollbackFor = Exception.class)
    public SystemNoticeDO revoke(String id) {
        SystemNoticeDO notice = this.detail(id);
        notice.setPublishStatus(false);
        notice.setPublishTime(null);
        AssertUtils.isFalse(notice.updateById(), "撤回通知失败");
        userNoticeService.removeByNoticeId(notice.getId());
        return notice;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeSingle(String id) {
        userNoticeService.removeByNoticeId(id);
        super.removeSingle(id);
    }

    private void normalizeAndValidate(SystemNoticeDO notice) {
        AssertUtils.isNull(notice, "通知公告不能为空");
        AssertUtils.isBlank(notice.getTitle(), "通知标题不能为空");
        AssertUtils.isBlank(notice.getNoticeType(), "通知类型不能为空");
        if (notice.getPublishStatus() == null) {
            notice.setPublishStatus(false);
        }
        if (Boolean.TRUE.equals(notice.getPublishStatus()) && notice.getPublishTime() == null) {
            notice.setPublishTime(new Date());
        }
        if (Boolean.FALSE.equals(notice.getPublishStatus())) {
            notice.setPublishTime(null);
        }
    }
}
