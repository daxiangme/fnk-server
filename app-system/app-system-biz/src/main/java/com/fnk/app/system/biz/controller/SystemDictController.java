package com.fnk.app.system.biz.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckPermission;
import com.fnk.app.system.api.model.query.SystemDictItemQuery;
import com.fnk.app.system.api.model.query.SystemDictTypeQuery;
import com.fnk.app.system.api.model.request.SystemDictItemAO;
import com.fnk.app.system.api.model.request.SystemDictTypeAO;
import com.fnk.app.system.api.model.response.SystemDictItemVO;
import com.fnk.app.system.api.model.response.SystemDictTypeVO;
import com.fnk.app.system.biz.convert.SystemConvert;
import com.fnk.app.system.biz.service.SystemDictItemService;
import com.fnk.app.system.biz.service.SystemDictTypeService;
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
 * 系统字典控制层。
 *
 * @author Enigma
 */
@RestController
@RequestMapping("/system/dict")
@Tag(name = "系统字典", description = "系统字典相关接口")
@AllArgsConstructor
public class SystemDictController extends BaseController {
    private final SystemDictTypeService dictTypeService;
    private final SystemDictItemService dictItemService;

    @GetMapping("/type")
    @Operation(summary = "字典类型列表")
    @SaCheckPermission("system:dict:view")
    public RestResponse<PageVO<SystemDictTypeVO>> typePage(SystemDictTypeQuery query) {
        return RestResponse.ok(SystemConvert.toSystemDictTypePage(dictTypeService.page(query)));
    }

    @GetMapping("/type/all")
    @Operation(summary = "全部字典类型")
    @SaCheckPermission("system:dict:view")
    public RestResponse<List<SystemDictTypeVO>> typeListAll() {
        return RestResponse.ok(SystemConvert.toSystemDictTypeVOList(dictTypeService.listAll()));
    }

    @PostMapping("/type")
    @Operation(summary = "创建字典类型")
    @SaCheckPermission("system:dict:create")
    public RestResponse<SystemDictTypeVO> createType(@RequestBody @Validated SystemDictTypeAO req) {
        return RestResponse.ok(SystemConvert.toSystemDictTypeVO(dictTypeService.create(SystemConvert.toSystemDictTypeDO(req))));
    }

    @PutMapping("/type/{id}")
    @Operation(summary = "更新字典类型")
    @SaCheckPermission("system:dict:update")
    public RestResponse<SystemDictTypeVO> updateType(@PathVariable String id, @RequestBody @Validated SystemDictTypeAO req) {
        return RestResponse.ok(SystemConvert.toSystemDictTypeVO(dictTypeService.update(id, SystemConvert.toSystemDictTypeDO(req))));
    }

    @DeleteMapping("/type/{id}")
    @Operation(summary = "删除字典类型")
    @SaCheckPermission("system:dict:delete")
    public RestResponse<Void> deleteType(@PathVariable String id) {
        dictTypeService.removeType(id);
        return RestResponse.ok();
    }

    @GetMapping("/item")
    @Operation(summary = "字典项列表")
    @SaCheckPermission("system:dict:view")
    public RestResponse<PageVO<SystemDictItemVO>> itemPage(SystemDictItemQuery query) {
        return RestResponse.ok(SystemConvert.toSystemDictItemPage(dictItemService.page(query)));
    }

    @PostMapping("/item")
    @Operation(summary = "创建字典项")
    @SaCheckPermission("system:dict:create")
    public RestResponse<SystemDictItemVO> createItem(@RequestBody @Validated SystemDictItemAO req) {
        return RestResponse.ok(SystemConvert.toSystemDictItemVO(dictItemService.create(SystemConvert.toSystemDictItemDO(req))));
    }

    @PutMapping("/item/{id}")
    @Operation(summary = "更新字典项")
    @SaCheckPermission("system:dict:update")
    public RestResponse<SystemDictItemVO> updateItem(@PathVariable String id, @RequestBody @Validated SystemDictItemAO req) {
        return RestResponse.ok(SystemConvert.toSystemDictItemVO(dictItemService.update(id, SystemConvert.toSystemDictItemDO(req))));
    }

    @DeleteMapping("/item/{id}")
    @Operation(summary = "删除字典项")
    @SaCheckPermission("system:dict:delete")
    public RestResponse<Void> deleteItem(@PathVariable String id) {
        dictItemService.removeSingle(id);
        return RestResponse.ok();
    }

    @GetMapping("/options/{dictCode}")
    @Operation(summary = "按字典编码查询启用字典项")
    @SaCheckLogin
    public RestResponse<List<SystemDictItemVO>> options(@PathVariable String dictCode) {
        return RestResponse.ok(SystemConvert.toSystemDictItemVOList(dictItemService.listEnabledByDictCode(dictCode)));
    }
}
