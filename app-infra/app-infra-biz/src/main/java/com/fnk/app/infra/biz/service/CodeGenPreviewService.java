package com.fnk.app.infra.biz.service;

import cn.hutool.core.util.StrUtil;
import com.fnk.app.infra.api.model.response.CodeGenFileVO;
import com.fnk.app.infra.api.model.response.CodeGenPreviewVO;
import com.fnk.app.infra.biz.convert.InfraConvert;
import com.fnk.app.infra.biz.dal.entity.CodeGenFieldDO;
import com.fnk.app.infra.biz.dal.entity.CodeGenRelationDO;
import com.fnk.app.infra.biz.dal.entity.CodeGenTableDO;
import com.fnk.common.bean.exception.LogicException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * 代码生成预览和下载服务。
 *
 * @author Enigma
 */
@Service
@RequiredArgsConstructor
public class CodeGenPreviewService {
    private static final Set<String> BASE_COLUMNS = Set.of("id", "create_time", "update_time", "deleted");

    private final CodeGenTableService tableService;
    private final CodeGenFieldService fieldService;
    private final CodeGenRelationService relationService;

    public CodeGenPreviewVO preview(String tableId) {
        CodeGenTableDO table = tableService.detail(tableId);
        List<CodeGenFieldDO> fields = fields(tableId);
        List<CodeGenRelationDO> relations = relationService.listByTableId(tableId);

        CodeGenPreviewVO preview = new CodeGenPreviewVO();
        preview.setTable(InfraConvert.toCodeGenTableVO(table));
        preview.setFields(InfraConvert.toCodeGenFieldVOList(fields));
        preview.setRelations(InfraConvert.toCodeGenRelationVOList(relations));
        preview.setFiles(renderFiles(table, fields, relations));
        return preview;
    }

    public byte[] downloadZip(String tableId) {
        CodeGenTableDO table = tableService.detail(tableId);
        List<CodeGenFileVO> files = renderFiles(table, fields(tableId), relationService.listByTableId(tableId));
        try (ByteArrayOutputStream output = new ByteArrayOutputStream();
             ZipOutputStream zip = new ZipOutputStream(output, StandardCharsets.UTF_8)) {
            for (CodeGenFileVO file : files) {
                zip.putNextEntry(new ZipEntry(file.getFilePath()));
                zip.write(file.getContent().getBytes(StandardCharsets.UTF_8));
                zip.closeEntry();
            }
            zip.finish();
            return output.toByteArray();
        } catch (IOException e) {
            throw new LogicException("生成代码压缩包失败");
        }
    }

    private List<CodeGenFieldDO> fields(String tableId) {
        return fieldService.listByTableId(tableId).stream()
                .sorted(Comparator.comparing(CodeGenFieldDO::getOrderSort, Comparator.nullsLast(Integer::compareTo)))
                .toList();
    }

    private List<CodeGenFileVO> renderFiles(CodeGenTableDO table, List<CodeGenFieldDO> fields, List<CodeGenRelationDO> relations) {
        GenMeta meta = GenMeta.from(table);
        List<CodeGenFileVO> files = new ArrayList<>();
        files.add(file("sql", "scripts/codegen/%s/schema.sql".formatted(table.getTableName()), renderSchemaSql(table, fields)));
        files.add(file("sql", "scripts/codegen/%s/menu-permission.sql".formatted(table.getTableName()), renderMenuSql(table, meta)));
        files.add(file("backend-api", meta.apiRequestPath(), renderAo(table, meta, fields)));
        files.add(file("backend-api", meta.apiQueryPath(), renderQuery(table, meta, fields)));
        files.add(file("backend-api", meta.apiVoPath(), renderVo(table, meta, fields)));
        files.add(file("backend-biz", meta.entityPath(), renderEntity(table, meta, fields)));
        files.add(file("backend-biz", meta.mapperPath(), renderMapper(table, meta)));
        files.add(file("backend-biz", meta.mapperXmlPath(), renderMapperXml(meta)));
        files.add(file("backend-biz", meta.servicePath(), renderService(table, meta, fields, relations)));
        files.add(file("backend-biz", meta.convertPath(), renderConvert(meta)));
        files.add(file("backend-biz", meta.controllerPath(), renderController(table, meta)));
        files.add(file("frontend", meta.frontendApiPath(), renderFrontendApi(table, meta, fields)));
        files.add(file("frontend", meta.frontendIndexPath(), renderVueIndex(table, meta, fields)));
        files.add(file("frontend", meta.frontendFormPath(), renderVueForm(table, meta, fields)));
        return files;
    }

    private CodeGenFileVO file(String type, String path, String content) {
        CodeGenFileVO file = new CodeGenFileVO();
        file.setFileType(type);
        file.setFilePath(path);
        file.setContent(content);
        return file;
    }

    private String renderAo(CodeGenTableDO table, GenMeta meta, List<CodeGenFieldDO> fields) {
        List<CodeGenFieldDO> formFields = fields.stream().filter(this::isFormField).toList();
        Set<String> imports = javaTypeImports(formFields);
        if (formFields.stream().anyMatch(this::isRequiredString)) {
            imports.add("jakarta.validation.constraints.NotBlank");
        }
        if (formFields.stream().anyMatch(field -> Boolean.TRUE.equals(field.getRequired()) && !isString(field))) {
            imports.add("jakarta.validation.constraints.NotNull");
        }

        StringBuilder builder = new StringBuilder();
        builder.append("package ").append(meta.apiPackage()).append(".model.request;\n\n");
        builder.append("import io.swagger.v3.oas.annotations.media.Schema;\n");
        builder.append("import lombok.Data;\n");
        appendImports(builder, imports);
        builder.append("\n/**\n * ").append(table.getBusinessName()).append("保存参数。\n *\n * @author ").append(author(table)).append("\n */\n");
        builder.append("@Data\n@Schema(name = \"").append(meta.className()).append("AO\", description = \"").append(table.getBusinessName()).append("保存参数\")\n");
        builder.append("public class ").append(meta.className()).append("AO {\n");
        for (CodeGenFieldDO field : formFields) {
            appendSchemaAndValidate(builder, field);
            builder.append("    private ").append(javaType(field)).append(" ").append(field.getPropertyName()).append(";\n\n");
        }
        builder.append("}\n");
        return builder.toString();
    }

    private String renderQuery(CodeGenTableDO table, GenMeta meta, List<CodeGenFieldDO> fields) {
        List<CodeGenFieldDO> queryFields = fields.stream().filter(this::isSearchField).toList();
        Set<String> imports = queryTypeImports(queryFields);
        StringBuilder builder = new StringBuilder();
        builder.append("package ").append(meta.apiPackage()).append(".model.query;\n\n");
        builder.append("import com.fnk.common.db.dto.SplitPageDTO;\n");
        builder.append("import io.swagger.v3.oas.annotations.media.Schema;\n");
        builder.append("import lombok.Data;\n");
        builder.append("import lombok.EqualsAndHashCode;\n");
        appendImports(builder, imports);
        builder.append("\n/**\n * ").append(table.getBusinessName()).append("分页查询参数。\n *\n * @author ").append(author(table)).append("\n */\n");
        builder.append("@Data\n@EqualsAndHashCode(callSuper = true)\n@Schema(name = \"").append(meta.className()).append("Query\", description = \"").append(table.getBusinessName()).append("分页查询参数\")\n");
        builder.append("public class ").append(meta.className()).append("Query extends SplitPageDTO {\n");
        for (CodeGenFieldDO field : queryFields) {
            builder.append("    @Schema(description = \"").append(javaEsc(fieldComment(field))).append("\")\n");
            builder.append("    private ").append(queryJavaType(field)).append(" ").append(field.getPropertyName()).append(";\n\n");
        }
        builder.append("}\n");
        return builder.toString();
    }

    private String renderVo(CodeGenTableDO table, GenMeta meta, List<CodeGenFieldDO> fields) {
        List<CodeGenFieldDO> voFields = fields.stream()
                .filter(field -> !"deleted".equals(field.getColumnName()))
                .toList();
        Set<String> imports = javaTypeImports(voFields);
        StringBuilder builder = new StringBuilder();
        builder.append("package ").append(meta.apiPackage()).append(".model.response;\n\n");
        builder.append("import io.swagger.v3.oas.annotations.media.Schema;\n");
        builder.append("import lombok.Data;\n");
        appendImports(builder, imports);
        builder.append("\n/**\n * ").append(table.getBusinessName()).append("返回对象。\n *\n * @author ").append(author(table)).append("\n */\n");
        builder.append("@Data\n@Schema(name = \"").append(meta.className()).append("VO\", description = \"").append(table.getBusinessName()).append("返回对象\")\n");
        builder.append("public class ").append(meta.className()).append("VO {\n");
        for (CodeGenFieldDO field : voFields) {
            builder.append("    @Schema(description = \"").append(javaEsc(fieldComment(field))).append("\")\n");
            builder.append("    private ").append(javaType(field)).append(" ").append(field.getPropertyName()).append(";\n\n");
        }
        builder.append("}\n");
        return builder.toString();
    }

    private String renderEntity(CodeGenTableDO table, GenMeta meta, List<CodeGenFieldDO> fields) {
        List<CodeGenFieldDO> entityFields = fields.stream().filter(field -> !isBaseField(field)).toList();
        Set<String> imports = javaTypeImports(entityFields);
        StringBuilder builder = new StringBuilder();
        builder.append("package ").append(meta.bizPackage()).append(".dal.entity;\n\n");
        builder.append("import com.baomidou.mybatisplus.annotation.TableName;\n");
        builder.append("import com.fnk.common.db.entity.BaseEntity;\n");
        builder.append("import io.swagger.v3.oas.annotations.media.Schema;\n");
        builder.append("import lombok.Data;\n");
        builder.append("import lombok.EqualsAndHashCode;\n");
        appendImports(builder, imports);
        builder.append("\n/**\n * ").append(table.getBusinessName()).append("。\n *\n * @author ").append(author(table)).append("\n */\n");
        builder.append("@Data\n@EqualsAndHashCode(callSuper = true)\n@TableName(\"").append(table.getTableName()).append("\")\n");
        builder.append("@Schema(name = \"").append(meta.className()).append("DO\", description = \"").append(table.getBusinessName()).append("\")\n");
        builder.append("public class ").append(meta.className()).append("DO extends BaseEntity<").append(meta.className()).append("DO> {\n");
        for (CodeGenFieldDO field : entityFields) {
            builder.append("    @Schema(description = \"").append(javaEsc(fieldComment(field))).append("\")\n");
            builder.append("    private ").append(javaType(field)).append(" ").append(field.getPropertyName()).append(";\n\n");
        }
        builder.append("}\n");
        return builder.toString();
    }

    private String renderMapper(CodeGenTableDO table, GenMeta meta) {
        return """
                package %s.dal.mapper;

                import com.baomidou.mybatisplus.core.mapper.BaseMapper;
                import %s.dal.entity.%sDO;
                import org.apache.ibatis.annotations.Mapper;

                /**
                 * %s Mapper。
                 *
                 * @author %s
                 */
                @Mapper
                public interface %sMapper extends BaseMapper<%sDO> {
                }
                """.formatted(
                meta.bizPackage(),
                meta.bizPackage(),
                meta.className(),
                table.getBusinessName(),
                author(table),
                meta.className(),
                meta.className());
    }

    private String renderMapperXml(GenMeta meta) {
        return """
                <?xml version="1.0" encoding="UTF-8"?>
                <!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
                        "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
                <mapper namespace="%s.dal.mapper.%sMapper">
                </mapper>
                """.formatted(meta.bizPackage(), meta.className());
    }

    private String renderService(CodeGenTableDO table, GenMeta meta, List<CodeGenFieldDO> fields, List<CodeGenRelationDO> relations) {
        List<CodeGenFieldDO> queryFields = fields.stream().filter(this::isSearchField).toList();
        boolean hasStringQuery = queryFields.stream().anyMatch(field -> isString(field) && !isBetweenQuery(field));
        boolean hasBetweenQuery = queryFields.stream().anyMatch(this::isBetweenQuery);
        StringBuilder builder = new StringBuilder();
        builder.append("package ").append(meta.bizPackage()).append(".service;\n\n");
        if (hasStringQuery) {
            builder.append("import cn.hutool.core.util.StrUtil;\n");
        }
        builder.append("import ").append(meta.apiPackage()).append(".model.query.").append(meta.className()).append("Query;\n");
        builder.append("import ").append(meta.bizPackage()).append(".dal.entity.").append(meta.className()).append("DO;\n");
        builder.append("import ").append(meta.bizPackage()).append(".dal.mapper.").append(meta.className()).append("Mapper;\n");
        builder.append("import com.fnk.common.db.impl.BaseService;\n");
        builder.append("import com.fnk.common.db.vo.PageVO;\n");
        builder.append("import org.springframework.stereotype.Service;\n");
        builder.append("import org.springframework.transaction.annotation.Transactional;\n\n");
        if (hasBetweenQuery) {
            builder.append("import java.util.List;\n\n");
        }
        builder.append("/**\n * ").append(table.getBusinessName()).append("服务。\n");
        builder.append(" * 生成类型：").append(StrUtil.blankToDefault(table.getGenerateType(), "single")).append("\n");
        if (!relations.isEmpty()) {
            builder.append(" * 关系配置已进入生成上下文，本期基础 CRUD 不自动生成级联保存逻辑。\n");
        }
        builder.append(" *\n * @author ").append(author(table)).append("\n */\n");
        builder.append("@Service\npublic class ").append(meta.className()).append("Service extends BaseService<")
                .append(meta.className()).append("Mapper, ").append(meta.className()).append("DO> {\n\n");
        builder.append("    public PageVO<").append(meta.className()).append("DO> page(").append(meta.className()).append("Query query) {\n");
        if (queryFields.isEmpty()) {
            builder.append("        return this.basicPage(query, ").append(meta.className()).append("DO::getCreateTime, wrapper -> {});\n");
        } else {
            builder.append("        return this.basicPage(query, ").append(meta.className()).append("DO::getCreateTime, wrapper -> wrapper\n");
            for (int i = 0; i < queryFields.size(); i++) {
                CodeGenFieldDO field = queryFields.get(i);
                builder.append("                .").append(queryMethod(field)).append("(")
                        .append(queryCondition(field)).append(", ")
                        .append(meta.className()).append("DO::").append(getter(field));
                if (isBetweenQuery(field)) {
                    builder.append(", rangeValue(query.").append(getter(field)).append("(), 0), rangeValue(query.")
                            .append(getter(field)).append("(), 1))");
                } else {
                    builder.append(", query.").append(getter(field)).append("())");
                }
                builder.append(i == queryFields.size() - 1 ? ");\n" : "\n");
            }
        }
        builder.append("    }\n\n");
        builder.append("    @Override\n    @Transactional(rollbackFor = Exception.class)\n");
        builder.append("    public ").append(meta.className()).append("DO create(").append(meta.className()).append("DO req) {\n");
        builder.append("        return super.create(req);\n    }\n\n");
        builder.append("    @Override\n    @Transactional(rollbackFor = Exception.class)\n");
        builder.append("    public ").append(meta.className()).append("DO update(String id, ").append(meta.className()).append("DO req) {\n");
        builder.append("        return super.update(id, req);\n    }\n\n");
        builder.append("    @Transactional(rollbackFor = Exception.class)\n");
        builder.append("    public void delete(String id) {\n");
        builder.append("        this.removeSingle(id);\n    }\n\n");
        if (hasBetweenQuery) {
            builder.append("    private String rangeValue(List<String> range, int index) {\n");
            builder.append("        return range != null && range.size() > index ? range.get(index) : null;\n");
            builder.append("    }\n\n");
        }
        builder.append("    @Override\n");
        builder.append("    public String getServiceModelName() {\n");
        builder.append("        return \"").append(javaEsc(table.getBusinessName())).append("\";\n");
        builder.append("    }\n");
        builder.append("}\n");
        return builder.toString();
    }

    private String renderConvert(GenMeta meta) {
        return """
                package %s.convert;

                import %s.model.request.%sAO;
                import %s.model.response.%sVO;
                import %s.dal.entity.%sDO;
                import com.fnk.common.db.vo.PageVO;
                import org.springframework.beans.BeanUtils;

                import java.util.Collections;
                import java.util.List;

                /**
                 * %s 对象转换。
                 *
                 * @author %s
                 */
                public class %sConvert {
                    private %sConvert() {
                    }

                    public static %sDO toDO(%sAO source) {
                        if (source == null) {
                            return null;
                        }
                        %sDO target = new %sDO();
                        BeanUtils.copyProperties(source, target);
                        return target;
                    }

                    public static %sVO toVO(%sDO source) {
                        if (source == null) {
                            return null;
                        }
                        %sVO target = new %sVO();
                        BeanUtils.copyProperties(source, target);
                        return target;
                    }

                    public static List<%sVO> toVOList(List<%sDO> source) {
                        if (source == null) {
                            return Collections.emptyList();
                        }
                        return source.stream().map(%sConvert::toVO).toList();
                    }

                    public static PageVO<%sVO> toPage(PageVO<%sDO> source) {
                        PageVO<%sVO> target = new PageVO<>();
                        BeanUtils.copyProperties(source, target, "records");
                        target.setRecords(toVOList(source.getRecords()));
                        return target;
                    }
                }
                """.formatted(
                meta.bizPackage(),
                meta.apiPackage(), meta.className(),
                meta.apiPackage(), meta.className(),
                meta.bizPackage(), meta.className(),
                meta.className(),
                meta.author(),
                meta.className(),
                meta.className(),
                meta.className(), meta.className(),
                meta.className(), meta.className(),
                meta.className(), meta.className(),
                meta.className(), meta.className(),
                meta.className(), meta.className(),
                meta.className(),
                meta.className(), meta.className(),
                meta.className());
    }

    private String renderController(CodeGenTableDO table, GenMeta meta) {
        return """
                package %s.controller;

                import cn.dev33.satoken.annotation.SaCheckPermission;
                import %s.model.query.%sQuery;
                import %s.model.request.%sAO;
                import %s.model.response.%sVO;
                import %s.convert.%sConvert;
                import %s.service.%sService;
                import com.fnk.common.bean.http.RestResponse;
                import com.fnk.common.db.vo.PageVO;
                import com.fnk.starter.web.controller.BaseController;
                import io.swagger.v3.oas.annotations.Operation;
                import io.swagger.v3.oas.annotations.tags.Tag;
                import lombok.AllArgsConstructor;
                import org.springframework.validation.annotation.Validated;
                import org.springframework.web.bind.annotation.DeleteMapping;
                import org.springframework.web.bind.annotation.GetMapping;
                import org.springframework.web.bind.annotation.PathVariable;
                import org.springframework.web.bind.annotation.PostMapping;
                import org.springframework.web.bind.annotation.PutMapping;
                import org.springframework.web.bind.annotation.RequestBody;
                import org.springframework.web.bind.annotation.RequestMapping;
                import org.springframework.web.bind.annotation.RestController;

                /**
                 * %s控制层。
                 *
                 * @author %s
                 */
                @RestController
                @RequestMapping("%s")
                @Tag(name = "%s", description = "%s相关接口")
                @AllArgsConstructor
                public class %sController extends BaseController {
                    private final %sService %sService;

                    @GetMapping
                    @Operation(summary = "%s分页")
                    @SaCheckPermission("%s:view")
                    public RestResponse<PageVO<%sVO>> page(%sQuery query) {
                        return RestResponse.ok(%sConvert.toPage(%sService.page(query)));
                    }

                    @GetMapping("/{id}")
                    @Operation(summary = "%s详情")
                    @SaCheckPermission("%s:view")
                    public RestResponse<%sVO> detail(@PathVariable String id) {
                        return RestResponse.ok(%sConvert.toVO(%sService.detail(id)));
                    }

                    @PostMapping
                    @Operation(summary = "新增%s")
                    @SaCheckPermission("%s:create")
                    public RestResponse<%sVO> create(@RequestBody @Validated %sAO req) {
                        return RestResponse.ok(%sConvert.toVO(%sService.create(%sConvert.toDO(req))));
                    }

                    @PutMapping("/{id}")
                    @Operation(summary = "编辑%s")
                    @SaCheckPermission("%s:update")
                    public RestResponse<%sVO> update(@PathVariable String id, @RequestBody @Validated %sAO req) {
                        return RestResponse.ok(%sConvert.toVO(%sService.update(id, %sConvert.toDO(req))));
                    }

                    @DeleteMapping("/{id}")
                    @Operation(summary = "删除%s")
                    @SaCheckPermission("%s:delete")
                    public RestResponse<Void> delete(@PathVariable String id) {
                        %sService.delete(id);
                        return RestResponse.ok();
                    }
                }
                """.formatted(
                meta.bizPackage(),
                meta.apiPackage(), meta.className(),
                meta.apiPackage(), meta.className(),
                meta.apiPackage(), meta.className(),
                meta.bizPackage(), meta.className(),
                meta.bizPackage(), meta.className(),
                table.getBusinessName(),
                author(table),
                table.getApiBasePath(),
                table.getBusinessName(), table.getBusinessName(),
                meta.className(),
                meta.className(), meta.serviceVar(),
                table.getBusinessName(), table.getPermissionPrefix(),
                meta.className(), meta.className(), meta.className(), meta.serviceVar(),
                table.getBusinessName(), table.getPermissionPrefix(),
                meta.className(), meta.className(), meta.serviceVar(),
                table.getBusinessName(), table.getPermissionPrefix(),
                meta.className(), meta.className(), meta.className(), meta.serviceVar(), meta.className(),
                table.getBusinessName(), table.getPermissionPrefix(),
                meta.className(), meta.className(), meta.className(), meta.serviceVar(), meta.className(),
                table.getBusinessName(), table.getPermissionPrefix(),
                meta.serviceVar());
    }

    private String renderFrontendApi(CodeGenTableDO table, GenMeta meta, List<CodeGenFieldDO> fields) {
        StringBuilder builder = new StringBuilder();
        builder.append("import request from '@/utils/http'\n\n");
        builder.append("export interface ").append(meta.className()).append("Item {\n");
        for (CodeGenFieldDO field : fields.stream().filter(field -> !"deleted".equals(field.getColumnName())).toList()) {
            builder.append("  ").append(field.getPropertyName()).append("?: ").append(tsType(field)).append("\n");
        }
        builder.append("}\n\n");
        builder.append("export interface ").append(meta.className()).append("SearchParams {\n");
        builder.append("  page: number\n");
        builder.append("  pageSize: number\n");
        for (CodeGenFieldDO field : fields.stream().filter(this::isSearchField).toList()) {
            builder.append("  ").append(field.getPropertyName()).append("?: ").append(tsQueryType(field)).append("\n");
        }
        builder.append("}\n");
        builder.append("export type ").append(meta.className()).append("Payload = Partial<").append(meta.className()).append("Item>\n\n");
        builder.append("export function fetchGet").append(meta.className()).append("List(params: ").append(meta.className()).append("SearchParams) {\n");
        builder.append("  return request.get<Api.Common.PaginatedResponse<").append(meta.className()).append("Item>>({\n");
        builder.append("    url: '").append(table.getApiBasePath()).append("',\n");
        builder.append("    params,\n");
        builder.append("    paramsSerializer: { indexes: null }\n");
        builder.append("  })\n}\n\n");
        builder.append("export function fetchGet").append(meta.className()).append("Detail(id: string) {\n");
        builder.append("  return request.get<").append(meta.className()).append("Item>({ url: `").append(table.getApiBasePath()).append("/${id}` })\n}\n\n");
        builder.append("export function fetchSave").append(meta.className()).append("(data: ").append(meta.className()).append("Payload) {\n");
        builder.append("  if (data.id) {\n");
        builder.append("    return request.put<").append(meta.className()).append("Item>({ url: `").append(table.getApiBasePath()).append("/${data.id}`, data })\n");
        builder.append("  }\n");
        builder.append("  return request.post<").append(meta.className()).append("Item>({ url: '").append(table.getApiBasePath()).append("', data })\n");
        builder.append("}\n\n");
        builder.append("export function fetchDelete").append(meta.className()).append("(id: string) {\n");
        builder.append("  return request.del<void>({ url: `").append(table.getApiBasePath()).append("/${id}` })\n");
        builder.append("}\n");
        return builder.toString();
    }

    private String renderVueIndex(CodeGenTableDO table, GenMeta meta, List<CodeGenFieldDO> fields) {
        List<CodeGenFieldDO> searchFields = fields.stream().filter(this::isSearchField).toList();
        List<CodeGenFieldDO> listFields = fields.stream().filter(this::isListField).toList();
        String formComponent = meta.className() + "Form";
        String formFile = meta.kebabName() + "-form.vue";
        StringBuilder builder = new StringBuilder();
        builder.append("<template>\n");
        builder.append("  <div class=\"art-full-height\">\n");
        builder.append("    <ArtSearchBar\n");
        builder.append("      v-show=\"showSearchBar\"\n");
        builder.append("      v-model=\"query\"\n");
        builder.append("      :items=\"searchItems\"\n");
        builder.append("      :show-expand=\"false\"\n");
        builder.append("      @search=\"loadData\"\n");
        builder.append("      @reset=\"resetQuery\"\n");
        builder.append("    />\n\n");
        builder.append("    <ElCard class=\"art-table-card\" :style=\"{ marginTop: showSearchBar ? '12px' : '0' }\">\n");
        builder.append("      <ArtTableHeader v-model:columns=\"columnChecks\" v-model:showSearchBar=\"showSearchBar\" :loading=\"loading\" @refresh=\"refreshData\">\n");
        builder.append("        <template #left>\n");
        builder.append("          <ElButton type=\"primary\" v-auth=\"'").append(table.getPermissionPrefix()).append(":create'\" @click=\"openCreate\">新增").append(table.getBusinessName()).append("</ElButton>\n");
        builder.append("        </template>\n");
        builder.append("      </ArtTableHeader>\n\n");
        builder.append("      <FnkTable\n");
        builder.append("        :loading=\"loading\"\n");
        builder.append("        :data=\"data\"\n");
        builder.append("        :columns=\"columns\"\n");
        builder.append("        :pagination=\"pagination\"\n");
        builder.append("        @pagination:size-change=\"handleSizeChange\"\n");
        builder.append("        @pagination:current-change=\"handleCurrentChange\"\n");
        builder.append("      />\n");
        builder.append("    </ElCard>\n\n");
        builder.append("    <ElDialog v-model=\"dialogVisible\" :title=\"form.id ? '编辑").append(table.getBusinessName()).append("' : '新增").append(table.getBusinessName()).append("'\" width=\"680px\">\n");
        builder.append("      <").append(formComponent).append(" ref=\"formRef\" v-model=\"form\" />\n");
        builder.append("      <template #footer>\n");
        builder.append("        <ElButton @click=\"dialogVisible = false\">取消</ElButton>\n");
        builder.append("        <ElButton type=\"primary\" :loading=\"saving\" @click=\"submit\">保存</ElButton>\n");
        builder.append("      </template>\n");
        builder.append("    </ElDialog>\n");
        builder.append("  </div>\n");
        builder.append("</template>\n\n");
        builder.append("<script setup lang=\"ts\">\n");
        builder.append("  import { ElButton } from 'element-plus'\n");
        builder.append("  import type { SearchFormItem } from '@/components/core/forms/art-search-bar/index.vue'\n");
        builder.append("  import type { ColumnOption } from '@/types/component'\n");
        builder.append("  import { useCrudTable } from '@/hooks/core/useCrudTable'\n");
        builder.append("  import {\n");
        builder.append("    fetchDelete").append(meta.className()).append(",\n");
        builder.append("    fetchGet").append(meta.className()).append("List,\n");
        builder.append("    fetchSave").append(meta.className()).append(",\n");
        builder.append("    type ").append(meta.className()).append("Item,\n");
        builder.append("    type ").append(meta.className()).append("Payload,\n");
        builder.append("    type ").append(meta.className()).append("SearchParams\n");
        builder.append("  } from '@/api/").append(meta.kebabName()).append("'\n");
        builder.append("  import ").append(formComponent).append(" from './modules/").append(formFile).append("'\n\n");
        builder.append("  defineOptions({ name: '").append(meta.className()).append("' })\n\n");
        builder.append("  const showSearchBar = ref(true)\n\n");
        builder.append("  const searchItems: SearchFormItem[] = [\n");
        for (CodeGenFieldDO field : searchFields) {
            builder.append("    ").append(renderSearchItem(field)).append(",\n");
        }
        builder.append("  ]\n\n");
        builder.append("  const {\n");
        builder.append("    loading,\n    saving,\n    dialogVisible,\n    formRef,\n    query,\n    form,\n    data,\n    pagination,\n    columns,\n    columnChecks,\n    loadData,\n    resetQuery,\n    openCreate,\n    openEdit,\n    submit,\n    remove,\n    handleSizeChange,\n    handleCurrentChange,\n    refreshData\n");
        builder.append("  } = useCrudTable<").append(meta.className()).append("Item, ").append(meta.className()).append("SearchParams, ").append(meta.className()).append("Payload>({\n");
        builder.append("    defaultQuery: () => ({ page: 1, pageSize: 10 }),\n");
        builder.append("    defaultForm: () => ({\n");
        for (CodeGenFieldDO field : fields.stream().filter(this::isFormField).toList()) {
            builder.append("      ").append(field.getPropertyName()).append(": ").append(defaultTsValue(field)).append(",\n");
        }
        builder.append("    }),\n");
        builder.append("    listApi: fetchGet").append(meta.className()).append("List,\n");
        builder.append("    saveApi: fetchSave").append(meta.className()).append(",\n");
        builder.append("    removeApi: (row) => fetchDelete").append(meta.className()).append("(row.id || ''),\n");
        builder.append("    getEditForm: (row) => ({ ...row }),\n");
        builder.append("    removeOptions: { message: (row) => `确定删除").append(table.getBusinessName()).append("「${row.id || ''}」吗？` },\n");
        builder.append("    columnsFactory: (): ColumnOption<").append(meta.className()).append("Item>[] => [\n");
        builder.append("      { type: 'globalIndex', label: '序号', width: 70 },\n");
        for (CodeGenFieldDO field : listFields) {
            builder.append("      { prop: '").append(field.getPropertyName()).append("', label: '").append(jsEsc(fieldComment(field))).append("', minWidth: ").append(width(field)).append(" },\n");
        }
        builder.append("      {\n");
        builder.append("        type: 'operation',\n");
        builder.append("        prop: 'operation',\n");
        builder.append("        label: '操作',\n");
        builder.append("        width: 160,\n");
        builder.append("        fixed: 'right',\n");
        builder.append("        actions: [\n");
        builder.append("          { key: 'edit', label: '编辑', permission: '").append(table.getPermissionPrefix()).append(":update', onClick: (row) => openEdit(row) },\n");
        builder.append("          { key: 'delete', label: '删除', type: 'error', permission: '").append(table.getPermissionPrefix()).append(":delete', onClick: (row) => remove(row) }\n");
        builder.append("        ]\n");
        builder.append("      }\n");
        builder.append("    ]\n");
        builder.append("  })\n");
        builder.append("</script>\n");
        return builder.toString();
    }

    private String renderVueForm(CodeGenTableDO table, GenMeta meta, List<CodeGenFieldDO> fields) {
        List<CodeGenFieldDO> formFields = fields.stream().filter(this::isFormField).toList();
        StringBuilder builder = new StringBuilder();
        builder.append("<template>\n");
        builder.append("  <ElForm ref=\"formRef\" :model=\"model\" :rules=\"rules\" label-width=\"100px\">\n");
        for (CodeGenFieldDO field : formFields) {
            builder.append("    <ElFormItem label=\"").append(jsEsc(fieldComment(field))).append("\" prop=\"").append(field.getPropertyName()).append("\">\n");
            builder.append(renderFormControl(field));
            builder.append("    </ElFormItem>\n");
        }
        builder.append("  </ElForm>\n");
        builder.append("</template>\n\n");
        builder.append("<script setup lang=\"ts\">\n");
        builder.append("  import type { FormInstance, FormRules } from 'element-plus'\n");
        builder.append("  import type { ").append(meta.className()).append("Payload } from '@/api/").append(meta.kebabName()).append("'\n\n");
        builder.append("  defineOptions({ name: '").append(meta.className()).append("Form' })\n\n");
        builder.append("  const model = defineModel<").append(meta.className()).append("Payload>({ required: true })\n");
        builder.append("  const formRef = ref<FormInstance>()\n\n");
        builder.append("  const rules: FormRules = {\n");
        for (CodeGenFieldDO field : formFields.stream().filter(field -> Boolean.TRUE.equals(field.getRequired())).toList()) {
            builder.append("    ").append(field.getPropertyName()).append(": [{ required: true, message: '请输入").append(jsEsc(fieldComment(field))).append("', trigger: 'blur' }],\n");
        }
        builder.append("  }\n\n");
        builder.append("  async function validate() {\n");
        builder.append("    await formRef.value?.validate()\n");
        builder.append("  }\n\n");
        builder.append("  function clearValidate() {\n");
        builder.append("    formRef.value?.clearValidate()\n");
        builder.append("  }\n\n");
        builder.append("  defineExpose({ validate, clearValidate })\n");
        builder.append("</script>\n");
        return builder.toString();
    }

    private String renderSchemaSql(CodeGenTableDO table, List<CodeGenFieldDO> fields) {
        StringBuilder builder = new StringBuilder();
        builder.append("-- ").append(table.getBusinessName()).append("业务表结构\n");
        builder.append("CREATE TABLE IF NOT EXISTS `").append(table.getTableName()).append("` (\n");
        for (CodeGenFieldDO field : fields) {
            builder.append("  `").append(field.getColumnName()).append("` ").append(StrUtil.blankToDefault(field.getDbType(), "varchar(255)"));
            if (Boolean.TRUE.equals(field.getPrimaryKey())) {
                builder.append(" NOT NULL");
            } else if (Boolean.TRUE.equals(field.getRequired())) {
                builder.append(" NOT NULL");
            } else {
                builder.append(" DEFAULT NULL");
            }
            builder.append(" COMMENT '").append(sqlEsc(fieldComment(field))).append("',\n");
        }
        String primaryColumn = fields.stream()
                .filter(field -> Boolean.TRUE.equals(field.getPrimaryKey()))
                .map(CodeGenFieldDO::getColumnName)
                .findFirst()
                .orElse("id");
        builder.append("  PRIMARY KEY (`").append(primaryColumn).append("`) USING BTREE\n");
        builder.append(") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='").append(sqlEsc(table.getBusinessName())).append("';\n");
        return builder.toString();
    }

    private String renderMenuSql(CodeGenTableDO table, GenMeta meta) {
        String menuId = stableId(table.getPermissionPrefix() + ":view");
        String parentId = StrUtil.blankToDefault(table.getMenuParentId(), "0");
        String routeKey = routeKey(table.getPermissionPrefix());
        String remark = "代码生成：" + table.getBusinessName();
        StringBuilder builder = new StringBuilder();
        builder.append("-- ").append(table.getBusinessName()).append("菜单和按钮权限\n");
        builder.append("REPLACE INTO `system_menu` (`id`, `root_id`, `name`, `route_key`, `order_sort`, `is_iframe`, `path`, `icon`, `local_icon`, `visible`, `permission`, `type`, `remark`, `deleted`, `create_time`, `update_time`) VALUES\n");
        builder.append("('").append(menuId).append("', '").append(parentId).append("', '").append(sqlEsc(table.getBusinessName())).append("', '").append(routeKey).append("', 1, 0, '").append(sqlEsc(table.getRoutePath())).append("', 'ri:file-list-3-line', NULL, 1, '").append(sqlEsc(table.getPermissionPrefix())).append(":view', 'MENU', '").append(sqlEsc(remark)).append("', 0, NOW(), NOW()),\n");
        builder.append(menuButtonSql(table, menuId, "create", "新增" + table.getBusinessName(), 1)).append(",\n");
        builder.append(menuButtonSql(table, menuId, "update", "编辑" + table.getBusinessName(), 2)).append(",\n");
        builder.append(menuButtonSql(table, menuId, "delete", "删除" + table.getBusinessName(), 3)).append(";\n");
        builder.append("\n-- 执行后请在菜单管理点击“刷新缓存数据”，让角色权限缓存立即失效。\n");
        return builder.toString();
    }

    private String menuButtonSql(CodeGenTableDO table, String menuId, String action, String name, int sort) {
        String permission = table.getPermissionPrefix() + ":" + action;
        return "('" + stableId(permission) + "', '" + menuId + "', '" + sqlEsc(name) + "', '" + routeKey(permission) + "', " + sort
                + ", 0, '', NULL, NULL, 0, '" + sqlEsc(permission) + "', 'BUTTON', '代码生成：" + sqlEsc(name) + "', 0, NOW(), NOW())";
    }

    private void appendSchemaAndValidate(StringBuilder builder, CodeGenFieldDO field) {
        builder.append("    @Schema(description = \"").append(javaEsc(fieldComment(field))).append("\")\n");
        if (Boolean.TRUE.equals(field.getRequired())) {
            if (isString(field)) {
                builder.append("    @NotBlank(message = \"").append(javaEsc(fieldComment(field))).append("不能为空\")\n");
            } else {
                builder.append("    @NotNull(message = \"").append(javaEsc(fieldComment(field))).append("不能为空\")\n");
            }
        }
    }

    private void appendImports(StringBuilder builder, Set<String> imports) {
        for (String item : imports) {
            builder.append("import ").append(item).append(";\n");
        }
    }

    private Set<String> javaTypeImports(List<CodeGenFieldDO> fields) {
        Set<String> imports = new LinkedHashSet<>();
        for (CodeGenFieldDO field : fields) {
            String type = StrUtil.blankToDefault(field.getJavaType(), "String");
            if (type.contains(".")) {
                imports.add(type);
                continue;
            }
            switch (type) {
                case "Date" -> imports.add("java.util.Date");
                case "LocalDate" -> imports.add("java.time.LocalDate");
                case "LocalDateTime" -> imports.add("java.time.LocalDateTime");
                case "BigDecimal" -> imports.add("java.math.BigDecimal");
                default -> {
                }
            }
        }
        return imports;
    }

    private Set<String> queryTypeImports(List<CodeGenFieldDO> fields) {
        Set<String> imports = new LinkedHashSet<>();
        if (fields.stream().anyMatch(this::isBetweenQuery)) {
            imports.add("java.util.List");
        }
        fields.stream()
                .filter(field -> !isBetweenQuery(field))
                .forEach(field -> imports.addAll(javaTypeImports(List.of(field))));
        return imports;
    }

    private String renderSearchItem(CodeGenFieldDO field) {
        boolean between = isBetweenQuery(field);
        String formType = StrUtil.blankToDefault(field.getFormType(), "input");
        String type = switch (formType) {
            case "number" -> "number";
            case "date" -> "date";
            case "datetime" -> "datetime";
            case "select", "dict-select", "switch" -> "select";
            default -> "input";
        };
        if (between && "date".equals(formType)) {
            return "{ label: '%s', key: '%s', type: 'daterange', props: { type: 'daterange', clearable: true, valueFormat: 'YYYY-MM-DD', startPlaceholder: '开始%s', endPlaceholder: '结束%s' } }"
                    .formatted(jsEsc(fieldComment(field)), field.getPropertyName(), jsEsc(fieldComment(field)), jsEsc(fieldComment(field)));
        }
        if (between && "datetime".equals(formType)) {
            return "{ label: '%s', key: '%s', type: 'datetimerange', props: { type: 'datetimerange', clearable: true, valueFormat: 'YYYY-MM-DD HH:mm:ss', startPlaceholder: '开始%s', endPlaceholder: '结束%s' } }"
                    .formatted(jsEsc(fieldComment(field)), field.getPropertyName(), jsEsc(fieldComment(field)), jsEsc(fieldComment(field)));
        }
        return "{ label: '%s', key: '%s', type: '%s', props: { clearable: true, placeholder: '请输入%s' } }"
                .formatted(jsEsc(fieldComment(field)), field.getPropertyName(), type, jsEsc(fieldComment(field)));
    }

    private String renderFormControl(CodeGenFieldDO field) {
        String prop = field.getPropertyName();
        String placeholder = jsEsc(fieldComment(field));
        return switch (StrUtil.blankToDefault(field.getFormType(), "input")) {
            case "textarea" -> "      <ElInput v-model=\"model.%s\" type=\"textarea\" :rows=\"4\" placeholder=\"请输入%s\" />\n".formatted(prop, placeholder);
            case "number" -> "      <ElInputNumber v-model=\"model.%s\" controls-position=\"right\" class=\"w-full\" />\n".formatted(prop);
            case "switch" -> "      <ElSwitch v-model=\"model.%s\" />\n".formatted(prop);
            case "date" -> "      <ElDatePicker v-model=\"model.%s\" type=\"date\" value-format=\"YYYY-MM-DD\" placeholder=\"请选择%s\" class=\"w-full\" />\n".formatted(prop, placeholder);
            case "datetime" -> "      <ElDatePicker v-model=\"model.%s\" type=\"datetime\" value-format=\"YYYY-MM-DD HH:mm:ss\" placeholder=\"请选择%s\" class=\"w-full\" />\n".formatted(prop, placeholder);
            case "select", "dict-select" -> "      <ElSelect v-model=\"model.%s\" clearable placeholder=\"请选择%s\" class=\"w-full\" />\n".formatted(prop, placeholder);
            default -> "      <ElInput v-model=\"model.%s\" placeholder=\"请输入%s\" />\n".formatted(prop, placeholder);
        };
    }

    private String queryMethod(CodeGenFieldDO field) {
        if (isBetweenQuery(field)) {
            return "between";
        }
        if ("like".equalsIgnoreCase(field.getQueryType()) && isString(field)) {
            return "like";
        }
        return "eq";
    }

    private String queryCondition(CodeGenFieldDO field) {
        if (isBetweenQuery(field)) {
            return "query." + getter(field) + "() != null && query." + getter(field) + "().size() == 2";
        }
        if (isString(field)) {
            return "StrUtil.isNotBlank(query." + getter(field) + "())";
        }
        return "query." + getter(field) + "() != null";
    }

    private String getter(CodeGenFieldDO field) {
        return "get" + StrUtil.upperFirst(field.getPropertyName());
    }

    private String defaultTsValue(CodeGenFieldDO field) {
        return switch (tsType(field)) {
            case "number" -> "undefined";
            case "boolean" -> "false";
            default -> "''";
        };
    }

    private String javaType(CodeGenFieldDO field) {
        String type = StrUtil.blankToDefault(field.getJavaType(), "String");
        if (type.contains(".")) {
            return type.substring(type.lastIndexOf('.') + 1);
        }
        return type;
    }

    private String queryJavaType(CodeGenFieldDO field) {
        if (isBetweenQuery(field)) {
            return "List<String>";
        }
        return javaType(field);
    }

    private String tsType(CodeGenFieldDO field) {
        return StrUtil.blankToDefault(field.getTsType(), "string");
    }

    private String tsQueryType(CodeGenFieldDO field) {
        if (isBetweenQuery(field)) {
            return "string[]";
        }
        return tsType(field);
    }

    private String fieldComment(CodeGenFieldDO field) {
        return StrUtil.blankToDefault(field.getColumnComment(), field.getColumnName());
    }

    private int width(CodeGenFieldDO field) {
        return field.getWidth() == null || field.getWidth() <= 0 ? 140 : field.getWidth();
    }

    private boolean isString(CodeGenFieldDO field) {
        return "String".equals(javaType(field));
    }

    private boolean isRequiredString(CodeGenFieldDO field) {
        return Boolean.TRUE.equals(field.getRequired()) && isString(field);
    }

    private boolean isBaseField(CodeGenFieldDO field) {
        return BASE_COLUMNS.contains(field.getColumnName());
    }

    private boolean isFormField(CodeGenFieldDO field) {
        return Boolean.TRUE.equals(field.getFormVisible()) && !Boolean.TRUE.equals(field.getPrimaryKey()) && !isBaseField(field);
    }

    private boolean isSearchField(CodeGenFieldDO field) {
        return Boolean.TRUE.equals(field.getSearchVisible()) && !Boolean.TRUE.equals(field.getPrimaryKey()) && !"deleted".equals(field.getColumnName());
    }

    private boolean isListField(CodeGenFieldDO field) {
        return Boolean.TRUE.equals(field.getListVisible()) && !"deleted".equals(field.getColumnName());
    }

    private boolean isBetweenQuery(CodeGenFieldDO field) {
        return "between".equalsIgnoreCase(field.getQueryType());
    }

    private String author(CodeGenTableDO table) {
        return StrUtil.blankToDefault(table.getAuthor(), "Enigma");
    }

    private String routeKey(String value) {
        return value.replace(':', '_').replace('-', '_');
    }

    private String stableId(String value) {
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            byte[] bytes = digest.digest(value.getBytes(StandardCharsets.UTF_8));
            StringBuilder builder = new StringBuilder();
            for (byte item : bytes) {
                builder.append(String.format("%02x", item));
            }
            return builder.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new LogicException("生成菜单ID失败");
        }
    }

    private String sqlEsc(String value) {
        return StrUtil.blankToDefault(value, "").replace("'", "''");
    }

    private String javaEsc(String value) {
        return StrUtil.blankToDefault(value, "").replace("\\", "\\\\").replace("\"", "\\\"");
    }

    private String jsEsc(String value) {
        return StrUtil.blankToDefault(value, "").replace("\\", "\\\\").replace("'", "\\'");
    }

    private record GenMeta(CodeGenTableDO table, String className, String moduleName, String kebabName, String author) {
        static GenMeta from(CodeGenTableDO table) {
            String className = StrUtil.blankToDefault(table.getClassName(), StrUtil.upperFirst(StrUtil.toCamelCase(table.getTableName())));
            return new GenMeta(
                    table,
                    className,
                    StrUtil.blankToDefault(table.getModuleName(), "infra"),
                    StrUtil.toSymbolCase(className, '-').toLowerCase(Locale.ROOT),
                    StrUtil.blankToDefault(table.getAuthor(), "Enigma")
            );
        }

        String packagePath() {
            return table.getPackageName().replace('.', '/');
        }

        String apiPackage() {
            return table.getPackageName() + ".api";
        }

        String bizPackage() {
            return table.getPackageName() + ".biz";
        }

        String serviceVar() {
            return StrUtil.lowerFirst(className) + "Service";
        }

        String moduleRoot() {
            return "app-" + moduleName;
        }

        String apiModuleRoot() {
            return moduleRoot() + "/app-" + moduleName + "-api/src/main/java/" + packagePath() + "/api";
        }

        String bizModuleRoot() {
            return moduleRoot() + "/app-" + moduleName + "-biz/src/main/java/" + packagePath() + "/biz";
        }

        String apiRequestPath() {
            return apiModuleRoot() + "/model/request/" + className + "AO.java";
        }

        String apiQueryPath() {
            return apiModuleRoot() + "/model/query/" + className + "Query.java";
        }

        String apiVoPath() {
            return apiModuleRoot() + "/model/response/" + className + "VO.java";
        }

        String entityPath() {
            return bizModuleRoot() + "/dal/entity/" + className + "DO.java";
        }

        String mapperPath() {
            return bizModuleRoot() + "/dal/mapper/" + className + "Mapper.java";
        }

        String mapperXmlPath() {
            return moduleRoot() + "/app-" + moduleName + "-biz/src/main/resources/mapper/" + className + "Mapper.xml";
        }

        String servicePath() {
            return bizModuleRoot() + "/service/" + className + "Service.java";
        }

        String convertPath() {
            return bizModuleRoot() + "/convert/" + className + "Convert.java";
        }

        String controllerPath() {
            return bizModuleRoot() + "/controller/" + className + "Controller.java";
        }

        String frontendApiPath() {
            return "frontend/src/api/" + kebabName + ".ts";
        }

        String frontendIndexPath() {
            return trimTrailingSlash(table.getFrontendPath()) + "/index.vue";
        }

        String frontendFormPath() {
            return trimTrailingSlash(table.getFrontendPath()) + "/modules/" + kebabName + "-form.vue";
        }

        private static String trimTrailingSlash(String value) {
            return StrUtil.removeSuffix(StrUtil.blankToDefault(value, "frontend/src/views/infra/generated"), "/");
        }
    }
}
