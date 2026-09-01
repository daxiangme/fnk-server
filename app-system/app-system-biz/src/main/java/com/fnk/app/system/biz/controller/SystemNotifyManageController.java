package com.fnk.app.system.biz.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.dev33.satoken.stp.StpUtil;
import com.fnk.app.system.api.model.query.SystemNotifyMessageQuery;
import com.fnk.app.system.api.model.query.SystemNotifyTemplateQuery;
import com.fnk.app.system.api.model.request.SystemNotifySendAO;
import com.fnk.app.system.api.model.request.SystemNotifyTemplateAO;
import com.fnk.app.system.api.model.response.SystemNotifyMessageVO;
import com.fnk.app.system.api.model.response.SystemNotifyTemplateVO;
import com.fnk.app.system.biz.convert.MessageCenterConvert;
import com.fnk.app.system.biz.service.SystemNotifyMessageService;
import com.fnk.app.system.biz.service.SystemNotifySendService;
import com.fnk.app.system.biz.service.SystemNotifyTemplateService;
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
 * 站内信管理控制层。
 *
 * @author Enigma
 */
@RestController
@RequestMapping("/system/messages/notify")
@Tag(name = "站内信管理", description = "站内信模板和消息记录")
@AllArgsConstructor
public class SystemNotifyManageController extends BaseController {
    private final SystemNotifyTemplateService templateService;
    private final SystemNotifyMessageService messageService;
    private final SystemNotifySendService sendService;

    @GetMapping("/template")
    @Operation(summary = "站内信模板列表")
    @SaCheckPermission("system:notify-template:view")
    public RestResponse<PageVO<SystemNotifyTemplateVO>> templatePage(SystemNotifyTemplateQuery query) {
        return RestResponse.ok(MessageCenterConvert.toNotifyTemplatePage(templateService.page(query)));
    }

    @GetMapping("/template/{id}")
    @Operation(summary = "站内信模板详情")
    @SaCheckPermission("system:notify-template:view")
    public RestResponse<SystemNotifyTemplateVO> templateDetail(@PathVariable String id) {
        return RestResponse.ok(MessageCenterConvert.toNotifyTemplateVO(templateService.detail(id)));
    }

    @PostMapping("/template")
    @Operation(summary = "创建站内信模板")
    @SaCheckPermission("system:notify-template:create")
    public RestResponse<SystemNotifyTemplateVO> createTemplate(@RequestBody @Validated SystemNotifyTemplateAO req) {
        return RestResponse.ok(MessageCenterConvert.toNotifyTemplateVO(templateService.create(MessageCenterConvert.toNotifyTemplateDO(req))));
    }

    @PutMapping("/template/{id}")
    @Operation(summary = "更新站内信模板")
    @SaCheckPermission("system:notify-template:update")
    public RestResponse<SystemNotifyTemplateVO> updateTemplate(@PathVariable String id,
                                                               @RequestBody @Validated SystemNotifyTemplateAO req) {
        return RestResponse.ok(MessageCenterConvert.toNotifyTemplateVO(templateService.update(id, MessageCenterConvert.toNotifyTemplateDO(req))));
    }

    @DeleteMapping("/template/{id}")
    @Operation(summary = "删除站内信模板")
    @SaCheckPermission("system:notify-template:delete")
    public RestResponse<Void> deleteTemplate(@PathVariable String id) {
        templateService.removeSingle(id);
        return RestResponse.ok();
    }

    @PostMapping("/template/send")
    @Operation(summary = "站内信模板测试发送")
    @SaCheckPermission("system:notify-template:send")
    public RestResponse<List<SystemNotifyMessageVO>> sendTemplate(@RequestBody @Validated SystemNotifySendAO req) {
        return RestResponse.ok(MessageCenterConvert.toNotifyMessageVOList(sendService.send(req)));
    }

    @GetMapping("/message")
    @Operation(summary = "站内信消息记录")
    @SaCheckPermission("system:notify-message:view")
    public RestResponse<PageVO<SystemNotifyMessageVO>> messagePage(SystemNotifyMessageQuery query) {
        return RestResponse.ok(MessageCenterConvert.toNotifyMessagePage(messageService.page(query)));
    }

    @GetMapping("/message/{id}")
    @Operation(summary = "站内信消息详情")
    @SaCheckPermission("system:notify-message:view")
    public RestResponse<SystemNotifyMessageVO> messageDetail(@PathVariable String id) {
        return RestResponse.ok(MessageCenterConvert.toNotifyMessageVO(messageService.detail(id)));
    }

    @GetMapping("/message/my")
    @Operation(summary = "我的站内信")
    @SaCheckLogin
    public RestResponse<PageVO<SystemNotifyMessageVO>> myMessagePage(SystemNotifyMessageQuery query) {
        return RestResponse.ok(MessageCenterConvert.toNotifyMessagePage(
                messageService.pageMy(StpUtil.getLoginIdAsString(), query)));
    }

    @GetMapping("/message/my/unread-list")
    @Operation(summary = "我的未读站内信")
    @SaCheckLogin
    public RestResponse<List<SystemNotifyMessageVO>> myUnreadList(@RequestParam(defaultValue = "10") Integer limit) {
        return RestResponse.ok(MessageCenterConvert.toNotifyMessageVOList(
                messageService.listUnread(StpUtil.getLoginIdAsString(), limit == null ? 10 : limit)));
    }

    @GetMapping("/message/my/unread-count")
    @Operation(summary = "我的未读站内信数量")
    @SaCheckLogin
    public RestResponse<Long> myUnreadCount() {
        return RestResponse.ok(messageService.unreadCount(StpUtil.getLoginIdAsString()));
    }

    @PostMapping("/message/my/{id}/read")
    @Operation(summary = "标记我的站内信已读")
    @SaCheckLogin
    public RestResponse<Void> readMyMessage(@PathVariable String id) {
        messageService.read(StpUtil.getLoginIdAsString(), id);
        return RestResponse.ok();
    }

    @PostMapping("/message/my/read-all")
    @Operation(summary = "标记我的全部站内信已读")
    @SaCheckLogin
    public RestResponse<Void> readAllMyMessage() {
        messageService.readAll(StpUtil.getLoginIdAsString());
        return RestResponse.ok();
    }
}
