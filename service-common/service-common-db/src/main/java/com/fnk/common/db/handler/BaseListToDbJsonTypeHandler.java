package com.fnk.common.db.handler;

import cn.hutool.json.JSONUtil;
import com.fnk.common.tools.utils.JacksonUtils;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 *  List 转换为 DbJson 类型的处理器
 * @author Enigma
 */

@MappedJdbcTypes(JdbcType.VARCHAR)
@MappedTypes({List.class})
public abstract class BaseListToDbJsonTypeHandler<T> extends BaseTypeHandler<List<T>> {
    private final Class<T> type;

    public BaseListToDbJsonTypeHandler(Class<T> type) {
        if (type == null) {
            throw new NullPointerException("请传入处理类型");
        }
        this.type = type;
    }

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, List<T> parameter, JdbcType jdbcType) throws SQLException {
        String s;
        try {
            s = JacksonUtils.getINSTANCE().writeValueAsString(parameter);
        } catch (Exception e) {
            e.printStackTrace();
            s = JSONUtil.toJsonStr(parameter);
        }
        ps.setString(i, s);
    }

    @Override
    public List<T> getNullableResult(ResultSet rs, String columnName) throws SQLException {
        String str = rs.getString(columnName);
        if (str == null) {
            return null;
        }
        return JacksonUtils.parseLongArray(rs.getString(columnName), this.type);
    }


    @Override
    public List<T> getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        String str = rs.getString(columnIndex);
        if (str == null) {
            return null;
        }
        return JSONUtil.toList(rs.getString(columnIndex), this.type);
    }

    @Override
    public List<T> getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        String str = cs.getString(columnIndex);
        if (str == null) {
            return null;
        }
        return JSONUtil.toList(cs.getString(columnIndex), this.type);
    }
}
