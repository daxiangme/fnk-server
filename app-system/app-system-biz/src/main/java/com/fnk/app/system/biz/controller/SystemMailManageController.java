package com.fnk.app.system.biz.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.fnk.app.system.api.model.query.SystemMailAccountQuery;
import com.fnk.app.system.api.model.query.SystemMailLogQuery;
import com.fnk.app.system.api.model.query.SystemMailTemplateQuery;
import com.fnk.app.system.api.model.request.SystemMailAccountAO;
import com.fnk.app.system.api.model.request.SystemMailSendAO;
import com.fnk.app.system.api.model.request.SystemMailTemplateAO;
import com.fnk.app.system.api.model.response.SystemMailAccountVO;
import com.fnk.app.system.api.model.response.SystemMailLogVO;
import com.fnk.app.system.api.model.response.SystemMailTemplateVO;
import com.fnk.app.system.biz.convert.MessageCenterConvert;
import com.fnk.app.system.biz.service.SystemMailAccountService;
import com.fnk.app.system.biz.service.SystemMailLogService;
import com.fnk.app.system.biz.service.SystemMailSendService;
import com.fnk.app.system.biz.service.SystemMailTemplateService;
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
 * 邮箱管理控制层。
 *
 * @author Enigma
 */
@RestController
@RequestMapping("/system/messages/mail")
@Tag(name = "邮箱管理", description = "邮箱账号、邮件模板和发送日志")
@AllArgsConstructor
public class SystemMailManageController extends BaseController {
    private final SystemMailAccountService accountService;
    private final SystemMailTemplateService templateService;
    private final SystemMailLogService logService;
    private final SystemMailSendService sendService;

    @GetMapping("/account")
    @Operation(summary = "邮箱账号列表")
    @SaCheckPermission("system:mail-account:view")
    public RestResponse<PageVO<SystemMailAccountVO>> accountPage(SystemMailAccountQuery query) {
        return RestResponse.ok(MessageCenterConvert.toMailAccountPage(accountService.page(query)));
    }

    @GetMapping("/account/enabled")
    @Operation(summary = "启用邮箱账号")
    @SaCheckPermission("system:mail-account:view")
    public RestResponse<List<SystemMailAccountVO>> enabledAccounts() {
        return RestResponse.ok(MessageCenterConvert.toMailAccountVOList(accountService.listEnabled()));
    }

    @GetMapping("/account/{id}")
    @Operation(summary = "邮箱账号详情")
    @SaCheckPermission("system:mail-account:view")
    public RestResponse<SystemMailAccountVO> accountDetail(@PathVariable String id) {
        return RestResponse.ok(MessageCenterConvert.toMailAccountVO(accountService.detail(id)));
    }

    @PostMapping("/account")
    @Operation(summary = "创建邮箱账号")
    @SaCheckPermission("system:mail-account:create")
    public RestResponse<SystemMailAccountVO> createAccount(@RequestBody @Validated SystemMailAccountAO req) {
        return RestResponse.ok(MessageCenterConvert.toMailAccountVO(accountService.create(MessageCenterConvert.toMailAccountDO(req))));
    }

    @PutMapping("/account/{id}")
    @Operation(summary = "更新邮箱账号")
    @SaCheckPermission("system:mail-account:update")
    public RestResponse<SystemMailAccountVO> updateAccount(@PathVariable String id,
                                                           @RequestBody @Validated SystemMailAccountAO req) {
        return RestResponse.ok(MessageCenterConvert.toMailAccountVO(accountService.update(id, MessageCenterConvert.toMailAccountDO(req))));
    }

    @DeleteMapping("/account/{id}")
    @Operation(summary = "删除邮箱账号")
    @SaCheckPermission("system:mail-account:delete")
    public RestResponse<Void> deleteAccount(@PathVariable String id) {
        accountService.removeSingle(id);
        return RestResponse.ok();
    }

    @GetMapping("/template")
    @Operation(summary = "邮件模板列表")
    @SaCheckPermission("system:mail-template:view")
    public RestResponse<PageVO<SystemMailTemplateVO>> templatePage(SystemMailTemplateQuery query) {
        return RestResponse.ok(MessageCenterConvert.toMailTemplatePage(templateService.page(query)));
    }

    @GetMapping("/template/{id}")
    @Operation(summary = "邮件模板详情")
    @SaCheckPermission("system:mail-template:view")
    public RestResponse<SystemMailTemplateVO> templateDetail(@PathVariable String id) {
        return RestResponse.ok(MessageCenterConvert.toMailTemplateVO(templateService.detail(id)));
    }

    @PostMapping("/template")
    @Operation(summary = "创建邮件模板")
    @SaCheckPermission("system:mail-template:create")
    public RestResponse<SystemMailTemplateVO> createTemplate(@RequestBody @Validated SystemMailTemplateAO req) {
        return RestResponse.ok(MessageCenterConvert.toMailTemplateVO(templateService.create(MessageCenterConvert.toMailTemplateDO(req))));
    }

    @PutMapping("/template/{id}")
    @Operation(summary = "更新邮件模板")
    @SaCheckPermission("system:mail-template:update")
    public RestResponse<SystemMailTemplateVO> updateTemplate(@PathVariable String id,
                                                             @RequestBody @Validated SystemMailTemplateAO req) {
        return RestResponse.ok(MessageCenterConvert.toMailTemplateVO(templateService.update(id, MessageCenterConvert.toMailTemplateDO(req))));
    }

    @DeleteMapping("/template/{id}")
    @Operation(summary = "删除邮件模板")
    @SaCheckPermission("system:mail-template:delete")
    public RestResponse<Void> deleteTemplate(@PathVariable String id) {
        templateService.removeSingle(id);
        return RestResponse.ok();
    }

    @PostMapping("/template/send")
    @Operation(summary = "邮件模板测试发送")
    @SaCheckPermission("system:mail-template:send")
    public RestResponse<SystemMailLogVO> sendTemplate(@RequestBody @Validated SystemMailSendAO req) {
        return RestResponse.ok(MessageCenterConvert.toMailLogVO(sendService.send(req)));
    }

    @GetMapping("/log")
    @Operation(summary = "邮件发送日志")
    @SaCheckPermission("system:mail-log:view")
    public RestResponse<PageVO<SystemMailLogVO>> logPage(SystemMailLogQuery query) {
        return RestResponse.ok(MessageCenterConvert.toMailLogPage(logService.page(query)));
    }

    @GetMapping("/log/{id}")
    @Operation(summary = "邮件日志详情")
    @SaCheckPermission("system:mail-log:view")
    public RestResponse<SystemMailLogVO> logDetail(@PathVariable String id) {
        return RestResponse.ok(MessageCenterConvert.toMailLogVO(logService.detail(id)));
    }
}
