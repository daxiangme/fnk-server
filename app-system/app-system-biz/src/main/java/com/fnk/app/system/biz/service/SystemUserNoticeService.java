package com.fnk.app.system.biz.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.fnk.app.system.api.model.query.SystemUserNoticeQuery;
import com.fnk.app.system.biz.dal.entity.AdminUserDO;
import com.fnk.app.system.biz.dal.entity.SystemNoticeDO;
import com.fnk.app.system.biz.dal.entity.SystemUserNoticeDO;
import com.fnk.app.system.biz.dal.mapper.SystemUserNoticeMapper;
import com.fnk.common.db.impl.BaseService;
import com.fnk.common.db.vo.PageVO;
import com.fnk.common.tools.lang.AssertUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * 用户站内通知服务。
 *
 * @author Enigma
 */
@Service
@RequiredArgsConstructor
public class SystemUserNoticeService extends BaseService<SystemUserNoticeMapper, SystemUserNoticeDO> {
    private final AdminUserService adminUserService;

    public PageVO<SystemUserNoticeDO> pageMy(String userId, SystemUserNoticeQuery query) {
        return this.basicPage(query, SystemUserNoticeDO::getCreateTime, wrapper -> wrapper
                .eq(SystemUserNoticeDO::getUserId, userId)
                .like(StrUtil.isNotBlank(query.getTitle()), SystemUserNoticeDO::getTitle, query.getTitle())
                .eq(StrUtil.isNotBlank(query.getNoticeType()), SystemUserNoticeDO::getNoticeType, query.getNoticeType())
                .eq(query.getReadStatus() != null, SystemUserNoticeDO::getReadStatus, query.getReadStatus()));
    }

    public long unreadCount(String userId) {
        return this.count(new LambdaQueryWrapper<SystemUserNoticeDO>()
                .eq(SystemUserNoticeDO::getUserId, userId)
                .eq(SystemUserNoticeDO::getReadStatus, false));
    }

    @Transactional(rollbackFor = Exception.class)
    public void deliver(SystemNoticeDO notice) {
        AssertUtils.isNull(notice, "通知公告不能为空");
        removeByNoticeId(notice.getId());
        if (!Boolean.TRUE.equals(notice.getPublishStatus())) {
            return;
        }

        List<AdminUserDO> users = adminUserService.list();
        if (CollUtil.isEmpty(users)) {
            return;
        }

        List<SystemUserNoticeDO> notices = users.stream()
                .map(user -> buildUserNotice(user.getId(), notice))
                .toList();
        this.create(notices);
    }

    @Transactional(rollbackFor = Exception.class)
    public void removeByNoticeId(String noticeId) {
        if (StrUtil.isBlank(noticeId)) {
            return;
        }
        this.remove(new LambdaQueryWrapper<SystemUserNoticeDO>()
                .eq(SystemUserNoticeDO::getNoticeId, noticeId));
    }

    public void read(String userId, String id) {
        SystemUserNoticeDO notice = this.detail(id, wrapper -> wrapper.eq(SystemUserNoticeDO::getUserId, userId));
        if (Boolean.TRUE.equals(notice.getReadStatus())) {
            return;
        }
        notice.setReadStatus(true);
        notice.setReadTime(new Date());
        AssertUtils.isFalse(notice.updateById(), "标记通知已读失败");
    }

    public void readAll(String userId) {
        this.update(new LambdaUpdateWrapper<SystemUserNoticeDO>()
                .eq(SystemUserNoticeDO::getUserId, userId)
                .eq(SystemUserNoticeDO::getReadStatus, false)
                .set(SystemUserNoticeDO::getReadStatus, true)
                .set(SystemUserNoticeDO::getReadTime, new Date()));
    }

    private SystemUserNoticeDO buildUserNotice(String userId, SystemNoticeDO notice) {
        SystemUserNoticeDO userNotice = new SystemUserNoticeDO();
        userNotice.setUserId(userId);
        userNotice.setNoticeId(notice.getId());
        userNotice.setTitle(notice.getTitle());
        userNotice.setNoticeType(notice.getNoticeType());
        userNotice.setContent(notice.getContent());
        userNotice.setReadStatus(false);
        return userNotice;
    }

    @Override
    public String getServiceModelName() {
        return "用户站内通知";
    }
}
