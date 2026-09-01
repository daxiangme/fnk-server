package com.fnk.app.system.biz.controller;

import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckPermission;
import com.fnk.app.system.api.model.query.SystemNoticeQuery;
import com.fnk.app.system.api.model.query.SystemUserNoticeQuery;
import com.fnk.app.system.api.model.request.SystemNoticeAO;
import com.fnk.app.system.api.model.response.SystemNoticeVO;
import com.fnk.app.system.api.model.response.SystemUserNoticeVO;
import com.fnk.app.system.biz.convert.SystemConvert;
import com.fnk.app.system.biz.service.SystemNoticeService;
import com.fnk.app.system.biz.service.SystemUserNoticeService;
import com.fnk.common.bean.http.RestResponse;
import com.fnk.common.db.vo.PageVO;
import com.fnk.starter.web.controller.BaseController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 通知公告控制层。
 *
 * @author Enigma
 */
@RestController
@RequestMapping("/system/notice")
@Tag(name = "通知公告", description = "通知公告相关接口")
@AllArgsConstructor
public class SystemNoticeController extends BaseController {
    private final SystemNoticeService noticeService;
    private final SystemUserNoticeService userNoticeService;

    @GetMapping
    @Operation(summary = "通知公告列表")
    @SaCheckPermission("system:notice:view")
    public RestResponse<PageVO<SystemNoticeVO>> page(SystemNoticeQuery query) {
        return RestResponse.ok(SystemConvert.toSystemNoticePage(noticeService.page(query)));
    }

    @GetMapping("/published")
    @Operation(summary = "已发布通知公告")
    @SaCheckLogin
    public RestResponse<List<SystemNoticeVO>> published() {
        return RestResponse.ok(SystemConvert.toSystemNoticeVOList(noticeService.listPublished()));
    }

    @GetMapping("/my")
    @Operation(summary = "我的站内通知")
    @SaCheckPermission("system:notice:mine")
    public RestResponse<PageVO<SystemUserNoticeVO>> myPage(SystemUserNoticeQuery query) {
        return RestResponse.ok(SystemConvert.toSystemUserNoticePage(
                userNoticeService.pageMy(StpUtil.getLoginIdAsString(), query)));
    }

    @GetMapping("/my/unread-count")
    @Operation(summary = "我的未读通知数量")
    @SaCheckPermission("system:notice:mine")
    public RestResponse<Long> unreadCount() {
        return RestResponse.ok(userNoticeService.unreadCount(StpUtil.getLoginIdAsString()));
    }

    @PostMapping("/my/{id}/read")
    @Operation(summary = "标记我的通知已读")
    @SaCheckPermission("system:notice:mine")
    public RestResponse<Void> read(@PathVariable String id) {
        userNoticeService.read(StpUtil.getLoginIdAsString(), id);
        return RestResponse.ok();
    }

    @PostMapping("/my/read-all")
    @Operation(summary = "标记全部我的通知已读")
    @SaCheckPermission("system:notice:mine")
    public RestResponse<Void> readAll() {
        userNoticeService.readAll(StpUtil.getLoginIdAsString());
        return RestResponse.ok();
    }

    @GetMapping("/{id}")
    @Operation(summary = "通知公告详情")
    @SaCheckPermission("system:notice:view")
    public RestResponse<SystemNoticeVO> detail(@PathVariable String id) {
        return RestResponse.ok(SystemConvert.toSystemNoticeVO(noticeService.detail(id)));
    }

    @PostMapping
    @Operation(summary = "创建通知公告")
    @SaCheckPermission("system:notice:create")
    public RestResponse<SystemNoticeVO> create(@RequestBody @Validated SystemNoticeAO req) {
        return RestResponse.ok(SystemConvert.toSystemNoticeVO(noticeService.create(SystemConvert.toSystemNoticeDO(req))));
    }

    @PutMapping("/{id}")
    @Operation(summary = "更新通知公告")
    @SaCheckPermission("system:notice:update")
    public RestResponse<SystemNoticeVO> update(@PathVariable String id, @RequestBody @Validated SystemNoticeAO req) {
        return RestResponse.ok(SystemConvert.toSystemNoticeVO(noticeService.update(id, SystemConvert.toSystemNoticeDO(req))));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除通知公告")
    @SaCheckPermission("system:notice:delete")
    public RestResponse<Void> delete(@PathVariable String id) {
        noticeService.removeSingle(id);
        return RestResponse.ok();
    }

    @PostMapping("/{id}/publish")
    @Operation(summary = "发布通知公告")
    @SaCheckPermission("system:notice:publish")
    public RestResponse<SystemNoticeVO> publish(@PathVariable String id) {
        return RestResponse.ok(SystemConvert.toSystemNoticeVO(noticeService.publish(id)));
    }

    @PostMapping("/{id}/revoke")
    @Operation(summary = "撤回通知公告")
    @SaCheckPermission("system:notice:revoke")
    public RestResponse<SystemNoticeVO> revoke(@PathVariable String id) {
        return RestResponse.ok(SystemConvert.toSystemNoticeVO(noticeService.revoke(id)));
    }
}
