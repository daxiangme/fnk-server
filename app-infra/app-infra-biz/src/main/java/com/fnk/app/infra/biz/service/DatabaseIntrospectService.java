package com.fnk.app.infra.biz.service;

import cn.hutool.core.util.StrUtil;
import com.fnk.app.infra.api.model.response.DatabaseTableVO;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 数据库结构读取服务。
 *
 * @author Enigma
 */
@Service
@RequiredArgsConstructor
public class DatabaseIntrospectService {
    private final JdbcTemplate jdbcTemplate;

    public List<DatabaseTableVO> listTables(Set<String> importedTables, String tableName) {
        String schema = currentSchema();
        String keyword = StrUtil.isBlank(tableName) ? null : "%" + tableName.trim() + "%";
        String sql = """
                SELECT t.TABLE_NAME,
                       t.TABLE_COMMENT,
                       t.ENGINE,
                       COUNT(c.COLUMN_NAME) AS COLUMN_COUNT
                FROM information_schema.TABLES t
                LEFT JOIN information_schema.COLUMNS c
                  ON c.TABLE_SCHEMA = t.TABLE_SCHEMA AND c.TABLE_NAME = t.TABLE_NAME
                WHERE t.TABLE_SCHEMA = ?
                  AND t.TABLE_TYPE = 'BASE TABLE'
                  AND (? IS NULL OR t.TABLE_NAME LIKE ?)
                GROUP BY t.TABLE_NAME, t.TABLE_COMMENT, t.ENGINE
                ORDER BY t.TABLE_NAME
                """;
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            DatabaseTableVO item = new DatabaseTableVO();
            item.setTableName(rs.getString("TABLE_NAME"));
            item.setTableComment(rs.getString("TABLE_COMMENT"));
            item.setEngine(rs.getString("ENGINE"));
            item.setColumnCount(rs.getInt("COLUMN_COUNT"));
            item.setImported(importedTables.contains(item.getTableName()));
            return item;
        }, schema, keyword, keyword);
    }

    public DatabaseTableMeta getTable(String tableName) {
        String schema = currentSchema();
        String sql = """
                SELECT TABLE_NAME, TABLE_COMMENT, ENGINE
                FROM information_schema.TABLES
                WHERE TABLE_SCHEMA = ?
                  AND TABLE_NAME = ?
                  AND TABLE_TYPE = 'BASE TABLE'
                """;
        List<DatabaseTableMeta> tables = jdbcTemplate.query(sql, (rs, rowNum) -> {
            DatabaseTableMeta item = new DatabaseTableMeta();
            item.setTableName(rs.getString("TABLE_NAME"));
            item.setTableComment(rs.getString("TABLE_COMMENT"));
            item.setEngine(rs.getString("ENGINE"));
            return item;
        }, schema, tableName);
        return tables.isEmpty() ? null : tables.get(0);
    }

    public List<DatabaseColumnMeta> listColumns(String tableName) {
        String schema = currentSchema();
        String sql = """
                SELECT COLUMN_NAME,
                       COLUMN_COMMENT,
                       DATA_TYPE,
                       COLUMN_TYPE,
                       IS_NULLABLE,
                       COLUMN_KEY,
                       ORDINAL_POSITION,
                       COLUMN_DEFAULT
                FROM information_schema.COLUMNS
                WHERE TABLE_SCHEMA = ?
                  AND TABLE_NAME = ?
                ORDER BY ORDINAL_POSITION
                """;
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            DatabaseColumnMeta item = new DatabaseColumnMeta();
            item.setColumnName(rs.getString("COLUMN_NAME"));
            item.setColumnComment(rs.getString("COLUMN_COMMENT"));
            item.setDataType(rs.getString("DATA_TYPE"));
            item.setColumnType(rs.getString("COLUMN_TYPE"));
            item.setNullable("YES".equalsIgnoreCase(rs.getString("IS_NULLABLE")));
            item.setPrimaryKey("PRI".equalsIgnoreCase(rs.getString("COLUMN_KEY")));
            item.setOrdinalPosition(rs.getInt("ORDINAL_POSITION"));
            item.setColumnDefault(rs.getString("COLUMN_DEFAULT"));
            return item;
        }, schema, tableName);
    }

    public List<ForeignKeyMeta> listForeignKeys(String tableName) {
        String schema = currentSchema();
        String sql = """
                SELECT COLUMN_NAME,
                       REFERENCED_TABLE_NAME,
                       REFERENCED_COLUMN_NAME,
                       CONSTRAINT_NAME
                FROM information_schema.KEY_COLUMN_USAGE
                WHERE TABLE_SCHEMA = ?
                  AND TABLE_NAME = ?
                  AND REFERENCED_TABLE_NAME IS NOT NULL
                """;
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            ForeignKeyMeta item = new ForeignKeyMeta();
            item.setColumnName(rs.getString("COLUMN_NAME"));
            item.setReferencedTableName(rs.getString("REFERENCED_TABLE_NAME"));
            item.setReferencedColumnName(rs.getString("REFERENCED_COLUMN_NAME"));
            item.setConstraintName(rs.getString("CONSTRAINT_NAME"));
            return item;
        }, schema, tableName);
    }

    public boolean tableExists(String tableName) {
        if (StrUtil.isBlank(tableName)) {
            return false;
        }
        String sql = """
                SELECT COUNT(1)
                FROM information_schema.TABLES
                WHERE TABLE_SCHEMA = ?
                  AND TABLE_NAME = ?
                """;
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, currentSchema(), tableName);
        return count != null && count > 0;
    }

    public String currentSchema() {
        String schema = jdbcTemplate.queryForObject("SELECT DATABASE()", String.class);
        return StrUtil.blankToDefault(schema, "");
    }

    public Map<String, String> readableColumns(String tableName) {
        return listColumns(tableName).stream()
                .collect(java.util.stream.Collectors.toMap(
                        DatabaseColumnMeta::getColumnName,
                        column -> StrUtil.blankToDefault(column.getColumnComment(), column.getColumnName()),
                        (left, right) -> left));
    }

    @Data
    public static class DatabaseTableMeta {
        private String tableName;
        private String tableComment;
        private String engine;
    }

    @Data
    public static class DatabaseColumnMeta {
        private String columnName;
        private String columnComment;
        private String dataType;
        private String columnType;
        private Boolean nullable;
        private Boolean primaryKey;
        private Integer ordinalPosition;
        private String columnDefault;
    }

    @Data
    public static class ForeignKeyMeta {
        private String columnName;
        private String referencedTableName;
        private String referencedColumnName;
        private String constraintName;
    }
}
