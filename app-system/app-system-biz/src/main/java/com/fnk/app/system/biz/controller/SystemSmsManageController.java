package com.fnk.app.system.biz.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.fnk.app.system.api.model.query.SystemSmsChannelQuery;
import com.fnk.app.system.api.model.query.SystemSmsLogQuery;
import com.fnk.app.system.api.model.query.SystemSmsTemplateQuery;
import com.fnk.app.system.api.model.request.SystemSmsChannelAO;
import com.fnk.app.system.api.model.request.SystemSmsSendAO;
import com.fnk.app.system.api.model.request.SystemSmsTemplateAO;
import com.fnk.app.system.api.model.response.SystemSmsChannelVO;
import com.fnk.app.system.api.model.response.SystemSmsLogVO;
import com.fnk.app.system.api.model.response.SystemSmsTemplateVO;
import com.fnk.app.system.biz.convert.MessageCenterConvert;
import com.fnk.app.system.biz.service.SystemSmsChannelService;
import com.fnk.app.system.biz.service.SystemSmsLogService;
import com.fnk.app.system.biz.service.SystemSmsSendService;
import com.fnk.app.system.biz.service.SystemSmsTemplateService;
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
 * 短信管理控制层。
 *
 * @author Enigma
 */
@RestController
@RequestMapping("/system/messages/sms")
@Tag(name = "短信管理", description = "短信渠道、模板和发送日志")
@AllArgsConstructor
public class SystemSmsManageController extends BaseController {
    private final SystemSmsChannelService channelService;
    private final SystemSmsTemplateService templateService;
    private final SystemSmsLogService logService;
    private final SystemSmsSendService sendService;

    @GetMapping("/channel")
    @Operation(summary = "短信渠道列表")
    @SaCheckPermission("system:sms-channel:view")
    public RestResponse<PageVO<SystemSmsChannelVO>> channelPage(SystemSmsChannelQuery query) {
        return RestResponse.ok(MessageCenterConvert.toSmsChannelPage(channelService.page(query)));
    }

    @GetMapping("/channel/enabled")
    @Operation(summary = "启用短信渠道")
    @SaCheckPermission("system:sms-channel:view")
    public RestResponse<List<SystemSmsChannelVO>> enabledChannels() {
        return RestResponse.ok(MessageCenterConvert.toSmsChannelVOList(channelService.listEnabled()));
    }

    @GetMapping("/channel/{id}")
    @Operation(summary = "短信渠道详情")
    @SaCheckPermission("system:sms-channel:view")
    public RestResponse<SystemSmsChannelVO> channelDetail(@PathVariable String id) {
        return RestResponse.ok(MessageCenterConvert.toSmsChannelVO(channelService.detail(id)));
    }

    @PostMapping("/channel")
    @Operation(summary = "创建短信渠道")
    @SaCheckPermission("system:sms-channel:create")
    public RestResponse<SystemSmsChannelVO> createChannel(@RequestBody @Validated SystemSmsChannelAO req) {
        return RestResponse.ok(MessageCenterConvert.toSmsChannelVO(channelService.create(MessageCenterConvert.toSmsChannelDO(req))));
    }

    @PutMapping("/channel/{id}")
    @Operation(summary = "更新短信渠道")
    @SaCheckPermission("system:sms-channel:update")
    public RestResponse<SystemSmsChannelVO> updateChannel(@PathVariable String id,
                                                          @RequestBody @Validated SystemSmsChannelAO req) {
        return RestResponse.ok(MessageCenterConvert.toSmsChannelVO(channelService.update(id, MessageCenterConvert.toSmsChannelDO(req))));
    }

    @DeleteMapping("/channel/{id}")
    @Operation(summary = "删除短信渠道")
    @SaCheckPermission("system:sms-channel:delete")
    public RestResponse<Void> deleteChannel(@PathVariable String id) {
        channelService.removeSingle(id);
        return RestResponse.ok();
    }

    @GetMapping("/template")
    @Operation(summary = "短信模板列表")
    @SaCheckPermission("system:sms-template:view")
    public RestResponse<PageVO<SystemSmsTemplateVO>> templatePage(SystemSmsTemplateQuery query) {
        return RestResponse.ok(MessageCenterConvert.toSmsTemplatePage(templateService.page(query)));
    }

    @GetMapping("/template/{id}")
    @Operation(summary = "短信模板详情")
    @SaCheckPermission("system:sms-template:view")
    public RestResponse<SystemSmsTemplateVO> templateDetail(@PathVariable String id) {
        return RestResponse.ok(MessageCenterConvert.toSmsTemplateVO(templateService.detail(id)));
    }

    @PostMapping("/template")
    @Operation(summary = "创建短信模板")
    @SaCheckPermission("system:sms-template:create")
    public RestResponse<SystemSmsTemplateVO> createTemplate(@RequestBody @Validated SystemSmsTemplateAO req) {
        return RestResponse.ok(MessageCenterConvert.toSmsTemplateVO(templateService.create(MessageCenterConvert.toSmsTemplateDO(req))));
    }

    @PutMapping("/template/{id}")
    @Operation(summary = "更新短信模板")
    @SaCheckPermission("system:sms-template:update")
    public RestResponse<SystemSmsTemplateVO> updateTemplate(@PathVariable String id,
                                                            @RequestBody @Validated SystemSmsTemplateAO req) {
        return RestResponse.ok(MessageCenterConvert.toSmsTemplateVO(templateService.update(id, MessageCenterConvert.toSmsTemplateDO(req))));
    }

    @DeleteMapping("/template/{id}")
    @Operation(summary = "删除短信模板")
    @SaCheckPermission("system:sms-template:delete")
    public RestResponse<Void> deleteTemplate(@PathVariable String id) {
        templateService.removeSingle(id);
        return RestResponse.ok();
    }

    @PostMapping("/template/send")
    @Operation(summary = "短信模板测试发送")
    @SaCheckPermission("system:sms-template:send")
    public RestResponse<SystemSmsLogVO> sendTemplate(@RequestBody @Validated SystemSmsSendAO req) {
        return RestResponse.ok(MessageCenterConvert.toSmsLogVO(sendService.send(req)));
    }

    @GetMapping("/log")
    @Operation(summary = "短信发送日志")
    @SaCheckPermission("system:sms-log:view")
    public RestResponse<PageVO<SystemSmsLogVO>> logPage(SystemSmsLogQuery query) {
        return RestResponse.ok(MessageCenterConvert.toSmsLogPage(logService.page(query)));
    }

    @GetMapping("/log/{id}")
    @Operation(summary = "短信日志详情")
    @SaCheckPermission("system:sms-log:view")
    public RestResponse<SystemSmsLogVO> logDetail(@PathVariable String id) {
        return RestResponse.ok(MessageCenterConvert.toSmsLogVO(logService.detail(id)));
    }
}
