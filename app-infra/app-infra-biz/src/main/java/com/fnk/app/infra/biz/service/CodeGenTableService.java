package com.fnk.app.infra.biz.service;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fnk.app.infra.api.model.query.CodeGenTableQuery;
import com.fnk.app.infra.api.model.request.CodeGenTableImportAO;
import com.fnk.app.infra.api.model.request.CodeGenTableUpdateAO;
import com.fnk.app.infra.api.model.response.DatabaseTableVO;
import com.fnk.app.infra.biz.dal.entity.CodeGenTableDO;
import com.fnk.app.infra.biz.dal.mapper.CodeGenTableMapper;
import com.fnk.common.db.impl.BaseService;
import com.fnk.common.db.vo.PageVO;
import com.fnk.common.tools.lang.AssertUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 代码生成表配置服务。
 *
 * @author Enigma
 */
@Service
@RequiredArgsConstructor
public class CodeGenTableService extends BaseService<CodeGenTableMapper, CodeGenTableDO> {
    private final DatabaseIntrospectService databaseIntrospectService;
    private final TypeMappingService typeMappingService;
    private final CodeGenFieldService fieldService;

    public PageVO<CodeGenTableDO> page(CodeGenTableQuery query) {
        return this.basicPage(query, CodeGenTableDO::getCreateTime, wrapper -> wrapper
                .like(StrUtil.isNotBlank(query.getTableName()), CodeGenTableDO::getTableName, query.getTableName())
                .like(StrUtil.isNotBlank(query.getTableComment()), CodeGenTableDO::getTableComment, query.getTableComment())
                .eq(StrUtil.isNotBlank(query.getModuleName()), CodeGenTableDO::getModuleName, query.getModuleName())
                .eq(StrUtil.isNotBlank(query.getGenerateType()), CodeGenTableDO::getGenerateType, query.getGenerateType()));
    }

    public List<DatabaseTableVO> listDatabaseTables(String tableName, boolean excludeImported) {
        Set<String> importedTables = this.list().stream()
                .map(CodeGenTableDO::getTableName)
                .collect(Collectors.toSet());
        return databaseIntrospectService.listTables(importedTables, tableName).stream()
                .filter(table -> !excludeImported || !Boolean.TRUE.equals(table.getImported()))
                .toList();
    }

    @Transactional(rollbackFor = Exception.class)
    public List<CodeGenTableDO> importTables(CodeGenTableImportAO req) {
        List<String> tableNames = req.getTableNames().stream()
                .map(String::trim)
                .collect(Collectors.collectingAndThen(
                        Collectors.toCollection(LinkedHashSet::new),
                        List::copyOf));
        AssertUtils.isEmpty(tableNames, "请选择需要导入的数据表");

        Set<String> importedTables = this.list(new LambdaQueryWrapper<CodeGenTableDO>()
                        .in(CodeGenTableDO::getTableName, tableNames))
                .stream()
                .map(CodeGenTableDO::getTableName)
                .collect(Collectors.toSet());
        AssertUtils.isTrue(!importedTables.isEmpty(),
                "数据表已导入：" + String.join("、", importedTables));

        List<DatabaseIntrospectService.DatabaseTableMeta> tableMetas = tableNames.stream()
                .map(tableName -> {
                    DatabaseIntrospectService.DatabaseTableMeta tableMeta = databaseIntrospectService.getTable(tableName);
                    AssertUtils.isNull(tableMeta, "数据表不存在：" + tableName);
                    return tableMeta;
                })
                .toList();

        List<CodeGenTableDO> importedConfigs = new ArrayList<>(tableMetas.size());
        for (DatabaseIntrospectService.DatabaseTableMeta tableMeta : tableMetas) {
            importedConfigs.add(importTable(tableMeta));
        }
        return importedConfigs;
    }

    private CodeGenTableDO importTable(DatabaseIntrospectService.DatabaseTableMeta tableMeta) {
        AssertUtils.isNull(tableMeta, "数据表不存在");

        CodeGenTableDO config = new CodeGenTableDO();
        config.setTableName(tableMeta.getTableName());
        config.setTableComment(tableMeta.getTableComment());

        String businessName = StrUtil.blankToDefault(tableMeta.getTableComment(), tableMeta.getTableName());
        String moduleName = "infra";
        String entityName = toUpperCamel(tableMeta.getTableName());
        String entityPath = StrUtil.toSymbolCase(entityName, '-');
        String routePath = "/" + moduleName + "/" + entityPath;

        config.setBusinessName(businessName);
        config.setModuleName(moduleName);
        config.setClassName(entityName);
        config.setPackageName("com.fnk.app." + moduleName);
        config.setApiBasePath("/" + moduleName + "/" + StrUtil.toSymbolCase(entityName, '-'));
        config.setFrontendPath("frontend/src/views/" + moduleName + "/" + entityPath);
        config.setRoutePath(routePath);
        config.setPermissionPrefix(moduleName + ":" + entityPath.replace("-", ":"));
        config.setGenerateType("single");
        config.setAuthor("Enigma");
        config.setSyncTime(new Date());

        this.create(config);
        syncFields(config.getId());
        return this.detail(config.getId());
    }

    @Transactional(rollbackFor = Exception.class)
    public CodeGenTableDO updateConfig(String id, CodeGenTableUpdateAO req) {
        CodeGenTableDO config = this.detail(id);
        BeanUtils.copyProperties(req, config);
        AssertUtils.isFalse(config.updateById(), "更新代码生成表配置失败");
        return this.detail(id);
    }

    @Transactional(rollbackFor = Exception.class)
    public void syncFields(String tableId) {
        CodeGenTableDO config = this.detail(tableId);
        List<DatabaseIntrospectService.DatabaseColumnMeta> columns = databaseIntrospectService.listColumns(config.getTableName());
        AssertUtils.isEmpty(columns, "数据表字段不存在");
        fieldService.syncFields(tableId, columns, typeMappingService);
        config.setSyncTime(new Date());
        this.updateById(config);
    }

    private String toUpperCamel(String tableName) {
        String camel = StrUtil.toCamelCase(tableName);
        return StrUtil.upperFirst(camel);
    }

    @Override
    public String getServiceModelName() {
        return "代码生成表配置";
    }
}
