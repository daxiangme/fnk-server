package ${package.Controller};

import ${package.Service}.${table.serviceName};
import ${package.Entity}.${entity};
import com.fnk.common.db.dto.SplitPageDTO;
import com.fnk.common.bean.http.RestResponse;
import com.fnk.common.db.vo.PageVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
<#if restControllerStyle>
<#else>
import org.springframework.stereotype.Controller;
</#if>
<#if superControllerClassPackage??>
import ${superControllerClassPackage};
</#if>

import java.util.List;

/**
* ${table.comment!} 控制层
*
* @author ${author}
*/
<#if restControllerStyle>
@RestController
<#else>
@Controller
</#if>
@RequestMapping("<#if package.ModuleName?? && package.ModuleName != "">/${package.ModuleName}</#if>/<#if controllerMappingHyphenStyle??>${controllerMappingHyphen?replace("-","/")?replace("_","/")}<#else>${table.entityPath?replace("-","/")?replace("_","/")}</#if>")
@Tag(name = "${table.comment!}", description = "${table.comment!}相关接口")
<#if superControllerClass??>
public class ${table.controllerName} extends ${superControllerClass} {
<#else>
public class ${table.controllerName} {
</#if>

    @Autowired
    private ${table.serviceName} service;

    @GetMapping
    @Operation(summary = "${table.comment!}列表")
    public RestResponse<PageVO<${entity}>> list(SplitPageDTO page,${entity} params) {
        return RestResponse.ok(this.service.basicPage(page, ${entity}::getCreateTime, (w) -> {
            // TODO 此处应去除不需要的查询条件
            <#list table.fields as field>
                <#if field.propertyType == "boolean">
                    <#assign getprefix="is"/>
                <#else>
                    <#assign getprefix="get"/>
                </#if>
                <#if field.propertyType == "String" || field.propertyType == "Long" || field.propertyType == "Integer" || field.propertyType == "Float" || field.propertyType == "Double">
            w.like(params.${getprefix}${field.capitalName}()!=null, ${entity}::${getprefix}${field.capitalName}, params.${getprefix}${field.capitalName}());
                <#elseif field.propertyType == "List" || field.propertyType == "Set" || field.propertyType == "Map">
            w.in(params.${getprefix}${field.capitalName}()!=null, ${entity}::${getprefix}${field.capitalName}, params.${getprefix}${field.capitalName}());
                <#else>
            w.eq(params.${getprefix}${field.capitalName}()!=null, ${entity}::${getprefix}${field.capitalName}, params.${getprefix}${field.capitalName}());
                </#if>
            </#list>
        }));
    }

    @GetMapping("/{id}")
    @Operation(summary = "获取指定ID${table.comment!}的详情")
    public RestResponse<${entity}> detail(@PathVariable String id) {
        return RestResponse.ok(this.service.detail(id));
    }

    @PostMapping
    @Operation(summary = "创建${table.comment!}")
    public RestResponse<${entity}> create(@RequestBody @Validated ${entity} req) {
        return RestResponse.ok(this.service.create(req));
    }

    @PutMapping("/{id}")
    @Operation(summary = "更新指定ID的${table.comment!}")
    public RestResponse<${entity}> update(@PathVariable String id, @RequestBody @Validated ${entity} req) {
        return RestResponse.ok(this.service.update(id, req));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除指定ID的${table.comment!}")
    public RestResponse<Void> remove(@PathVariable String id) {
        this.service.removeSingle(id);
        return RestResponse.ok();
    }

    @DeleteMapping
    @Operation(summary = "批量删除指定ID的${table.comment!}")
    public RestResponse<Void> remove(@RequestParam("id-list") List<String> idList) {
        this.service.remove(idList);
        return RestResponse.ok();
    }

}
