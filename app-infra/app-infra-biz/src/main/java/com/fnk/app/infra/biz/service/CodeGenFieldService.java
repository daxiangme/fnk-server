package com.fnk.app.infra.biz.service;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fnk.app.infra.api.model.request.CodeGenFieldBatchUpdateAO;
import com.fnk.app.infra.api.model.request.CodeGenFieldUpdateAO;
import com.fnk.app.infra.biz.dal.entity.CodeGenFieldDO;
import com.fnk.app.infra.biz.dal.mapper.CodeGenFieldMapper;
import com.fnk.common.db.impl.BaseService;
import com.fnk.common.tools.lang.AssertUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 代码生成字段配置服务。
 *
 * @author Enigma
 */
@Service
public class CodeGenFieldService extends BaseService<CodeGenFieldMapper, CodeGenFieldDO> {

    public List<CodeGenFieldDO> listByTableId(String tableId) {
        return this.list(new LambdaQueryWrapper<CodeGenFieldDO>()
                .eq(CodeGenFieldDO::getTableId, tableId)
                .orderByAsc(CodeGenFieldDO::getOrderSort));
    }

    @Transactional(rollbackFor = Exception.class)
    public void syncFields(String tableId, List<DatabaseIntrospectService.DatabaseColumnMeta> columns, TypeMappingService typeMappingService) {
        Map<String, CodeGenFieldDO> existing = listByTableId(tableId).stream()
                .collect(Collectors.toMap(CodeGenFieldDO::getColumnName, Function.identity(), (left, right) -> left));

        List<String> columnNames = columns.stream().map(DatabaseIntrospectService.DatabaseColumnMeta::getColumnName).toList();
        this.remove(new LambdaQueryWrapper<CodeGenFieldDO>()
                .eq(CodeGenFieldDO::getTableId, tableId)
                .notIn(!columnNames.isEmpty(), CodeGenFieldDO::getColumnName, columnNames));

        for (DatabaseIntrospectService.DatabaseColumnMeta column : columns) {
            CodeGenFieldDO field = existing.getOrDefault(column.getColumnName(), new CodeGenFieldDO());
            TypeMappingService.MappingResult mapping = typeMappingService.map(
                    column.getColumnName(),
                    column.getDataType(),
                    column.getColumnType(),
                    Boolean.TRUE.equals(column.getPrimaryKey()));

            if (StrUtil.isBlank(field.getId())) {
                field.setTableId(tableId);
                field.setColumnName(column.getColumnName());
                field.setPropertyName(StrUtil.toCamelCase(column.getColumnName()));
                field.setColumnComment(StrUtil.blankToDefault(column.getColumnComment(), column.getColumnName()));
                field.setDbType(column.getColumnType());
                field.setJavaType(mapping.getJavaType());
                field.setTsType(mapping.getTsType());
                field.setPrimaryKey(Boolean.TRUE.equals(column.getPrimaryKey()));
                field.setRequired(!Boolean.TRUE.equals(column.getNullable()) && !Boolean.TRUE.equals(column.getPrimaryKey()));
                field.setListVisible(!isBaseField(column.getColumnName()));
                field.setSearchVisible(!isBaseField(column.getColumnName()) && !Boolean.TRUE.equals(column.getPrimaryKey()));
                field.setFormVisible(!isBaseField(column.getColumnName()) && !Boolean.TRUE.equals(column.getPrimaryKey()));
                field.setDetailVisible(!isBaseField(column.getColumnName()));
                field.setFormType(mapping.getFormType());
                field.setQueryType(mapping.getQueryType());
                field.setDefaultValue(column.getColumnDefault());
                field.setOrderSort(column.getOrdinalPosition());
                field.setWidth(defaultWidth(mapping.getFormType()));
                field.setReadonlyOnCreate(false);
                field.setReadonlyOnEdit(Boolean.TRUE.equals(column.getPrimaryKey()));
                this.create(field);
            } else {
                field.setColumnComment(StrUtil.blankToDefault(field.getColumnComment(), column.getColumnComment()));
                field.setDbType(column.getColumnType());
                field.setPrimaryKey(Boolean.TRUE.equals(column.getPrimaryKey()));
                field.setOrderSort(column.getOrdinalPosition());
                this.updateById(field);
            }
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public List<CodeGenFieldDO> batchUpdate(String tableId, CodeGenFieldBatchUpdateAO req) {
        AssertUtils.isEmpty(req.getFields(), "字段配置不能为空");
        Map<String, CodeGenFieldDO> fieldMap = listByTableId(tableId).stream()
                .collect(Collectors.toMap(CodeGenFieldDO::getId, Function.identity()));
        for (CodeGenFieldUpdateAO item : req.getFields()) {
            CodeGenFieldDO field = fieldMap.get(item.getId());
            AssertUtils.isNull(field, "字段配置不存在");
            BeanUtils.copyProperties(item, field, "id");
            field.setTableId(tableId);
            AssertUtils.isFalse(field.updateById(), "更新字段配置失败");
        }
        return listByTableId(tableId).stream()
                .sorted(Comparator.comparing(CodeGenFieldDO::getOrderSort, Comparator.nullsLast(Integer::compareTo)))
                .toList();
    }

    private boolean isBaseField(String columnName) {
        return "id".equals(columnName)
                || "create_time".equals(columnName)
                || "update_time".equals(columnName)
                || "deleted".equals(columnName);
    }

    private int defaultWidth(String formType) {
        if ("datetime".equals(formType)) {
            return 180;
        }
        if ("switch".equals(formType)) {
            return 100;
        }
        return 140;
    }

    @Override
    public String getServiceModelName() {
        return "代码生成字段";
    }
}
