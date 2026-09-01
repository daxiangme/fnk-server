package com.fnk.app.infra.biz.service;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fnk.app.infra.api.enums.CodeGenRelationTypeEnum;
import com.fnk.app.infra.api.model.request.CodeGenRelationBatchUpdateAO;
import com.fnk.app.infra.api.model.request.CodeGenRelationUpdateAO;
import com.fnk.app.infra.biz.dal.entity.CodeGenFieldDO;
import com.fnk.app.infra.biz.dal.entity.CodeGenRelationDO;
import com.fnk.app.infra.biz.dal.entity.CodeGenTableDO;
import com.fnk.app.infra.biz.dal.mapper.CodeGenRelationMapper;
import com.fnk.common.db.impl.BaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

/**
 * 代码生成关系分析服务。
 *
 * @author Enigma
 */
@Service
@RequiredArgsConstructor
public class CodeGenRelationService extends BaseService<CodeGenRelationMapper, CodeGenRelationDO> {
    private final DatabaseIntrospectService databaseIntrospectService;
    private final CodeGenTableService tableService;
    private final CodeGenFieldService fieldService;

    public List<CodeGenRelationDO> listByTableId(String tableId) {
        return this.list(new LambdaQueryWrapper<CodeGenRelationDO>()
                .eq(CodeGenRelationDO::getTableId, tableId)
                .orderByDesc(CodeGenRelationDO::getConfidence)
                .orderByAsc(CodeGenRelationDO::getRelationName));
    }

    @Transactional(rollbackFor = Exception.class)
    public List<CodeGenRelationDO> batchUpdate(String tableId, CodeGenRelationBatchUpdateAO req) {
        CodeGenTableDO table = tableService.detail(tableId);
        this.remove(new LambdaQueryWrapper<CodeGenRelationDO>().eq(CodeGenRelationDO::getTableId, tableId));

        List<CodeGenRelationDO> relations = (req == null || req.getRelations() == null ? List.<CodeGenRelationUpdateAO>of() : req.getRelations())
                .stream()
                .map(item -> toRelation(table, item))
                .filter(Objects::nonNull)
                .toList();
        if (!relations.isEmpty()) {
            this.create(relations);
        }

        return listByTableId(tableId);
    }

    @Transactional(rollbackFor = Exception.class)
    public List<CodeGenRelationDO> analyzeAndSave(String tableId) {
        CodeGenTableDO table = tableService.detail(tableId);
        List<CodeGenFieldDO> fields = fieldService.listByTableId(tableId);
        List<CodeGenRelationDO> relations = new ArrayList<>();

        databaseIntrospectService.listForeignKeys(table.getTableName()).forEach(fk -> relations.add(buildRelation(
                tableId,
                "fk_" + fk.getColumnName(),
                CodeGenRelationTypeEnum.MANY_TO_ONE.name(),
                table.getTableName(),
                fk.getColumnName(),
                fk.getReferencedTableName(),
                fk.getReferencedColumnName(),
                null,
                null,
                null,
                100,
                "foreign_key",
                "数据库外键约束")));

        fields.stream()
                .filter(field -> "parent_id".equals(field.getColumnName()))
                .findFirst()
                .ifPresent(field -> relations.add(buildRelation(
                        tableId,
                        "tree_parent",
                        CodeGenRelationTypeEnum.TREE.name(),
                        table.getTableName(),
                        field.getColumnName(),
                        table.getTableName(),
                        "id",
                        null,
                        null,
                        null,
                        95,
                        "naming_rule",
                        "parent_id 树结构规则")));

        fields.stream()
                .filter(field -> field.getColumnName().endsWith("_id"))
                .filter(field -> !"id".equals(field.getColumnName()))
                .filter(field -> !"parent_id".equals(field.getColumnName()))
                .forEach(field -> findTargetTable(field.getColumnName()).stream().findFirst()
                        .ifPresent(targetTable -> relations.add(buildRelation(
                                tableId,
                                "lookup_" + field.getColumnName(),
                                CodeGenRelationTypeEnum.LOOKUP.name(),
                                table.getTableName(),
                                field.getColumnName(),
                                targetTable,
                                "id",
                                null,
                                null,
                                null,
                                75,
                                "naming_rule",
                                "*_id 字段命名规则"))));

        List<CodeGenFieldDO> idFields = fields.stream()
                .filter(field -> field.getColumnName().endsWith("_id"))
                .filter(field -> !"id".equals(field.getColumnName()))
                .filter(field -> !"parent_id".equals(field.getColumnName()))
                .toList();
        long businessFieldCount = fields.stream()
                .filter(field -> !List.of("id", "create_time", "update_time", "deleted").contains(field.getColumnName()))
                .count();
        if (idFields.size() == 2 && businessFieldCount <= 4) {
            relations.add(buildRelation(
                    tableId,
                    "many_to_many_" + idFields.get(0).getColumnName() + "_" + idFields.get(1).getColumnName(),
                    CodeGenRelationTypeEnum.MANY_TO_MANY.name(),
                    table.getTableName(),
                    idFields.get(0).getColumnName(),
                    findTargetTable(idFields.get(1).getColumnName()).stream().findFirst().orElse(""),
                    "id",
                    table.getTableName(),
                    idFields.get(0).getColumnName(),
                    idFields.get(1).getColumnName(),
                    80,
                    "naming_rule",
                    "两个外键字段组成的关系表候选"));
        }

        this.remove(new LambdaQueryWrapper<CodeGenRelationDO>().eq(CodeGenRelationDO::getTableId, tableId));
        if (!relations.isEmpty()) {
            this.create(relations);
            if (relations.stream().anyMatch(item -> CodeGenRelationTypeEnum.TREE.name().equals(item.getRelationType()))) {
                table.setGenerateType("tree");
                table.updateById();
            } else if (relations.stream().anyMatch(item -> CodeGenRelationTypeEnum.MANY_TO_MANY.name().equals(item.getRelationType()))) {
                table.setGenerateType("manyToMany");
                table.updateById();
            }
        }
        return listByTableId(tableId);
    }

    private List<String> findTargetTable(String columnName) {
        String base = StrUtil.removeSuffix(columnName, "_id");
        return List.of(
                        base,
                        base + "_info",
                        "system_" + base,
                        "admin_" + base,
                        "app_" + base)
                .stream()
                .filter(databaseIntrospectService::tableExists)
                .sorted(Comparator.comparingInt(String::length))
                .toList();
    }

    private CodeGenRelationDO toRelation(CodeGenTableDO table, CodeGenRelationUpdateAO item) {
        if (StrUtil.isBlank(item.getRelationName()) || StrUtil.isBlank(item.getRelationType())) {
            return null;
        }

        CodeGenRelationDO relation = new CodeGenRelationDO();
        BeanUtils.copyProperties(item, relation, "id");
        relation.setTableId(table.getId());
        relation.setSourceTable(table.getTableName());
        relation.setGenerateQuery(item.getGenerateQuery() == null || Boolean.TRUE.equals(item.getGenerateQuery()));
        relation.setGenerateForm(item.getGenerateForm() == null || Boolean.TRUE.equals(item.getGenerateForm()));
        relation.setGenerateDetail(item.getGenerateDetail() == null || Boolean.TRUE.equals(item.getGenerateDetail()));
        relation.setDeleteStrategy(StrUtil.blankToDefault(item.getDeleteStrategy(), "manual"));
        relation.setConfidence(100);
        relation.setSourceType("manual");
        return relation;
    }

    private CodeGenRelationDO buildRelation(
            String tableId,
            String relationName,
            String relationType,
            String sourceTable,
            String sourceColumn,
            String targetTable,
            String targetColumn,
            String joinTable,
            String joinSourceColumn,
            String joinTargetColumn,
            Integer confidence,
            String sourceType,
            String remark) {
        CodeGenRelationDO relation = new CodeGenRelationDO();
        relation.setTableId(tableId);
        relation.setRelationName(relationName);
        relation.setRelationType(relationType);
        relation.setSourceTable(sourceTable);
        relation.setSourceColumn(sourceColumn);
        relation.setTargetTable(targetTable);
        relation.setTargetColumn(targetColumn);
        relation.setJoinTable(joinTable);
        relation.setJoinSourceColumn(joinSourceColumn);
        relation.setJoinTargetColumn(joinTargetColumn);
        relation.setDisplayColumn("name");
        relation.setGenerateQuery(true);
        relation.setGenerateForm(!CodeGenRelationTypeEnum.TREE.name().equals(relationType));
        relation.setGenerateDetail(true);
        relation.setDeleteStrategy(CodeGenRelationTypeEnum.TREE.name().equals(relationType) ? "restrict" : "manual");
        relation.setConfidence(confidence);
        relation.setSourceType(sourceType);
        relation.setRemark(remark);
        return relation;
    }

    @Override
    public String getServiceModelName() {
        return "代码生成关系";
    }
}
