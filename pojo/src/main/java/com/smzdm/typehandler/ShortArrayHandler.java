package com.smzdm.typehandler;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.*;

/**
 * Created by Changdy on 2018/1/27.
 */
public class ShortArrayHandler extends BaseTypeHandler<Short[]> {

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, Short[] parameter, JdbcType jdbcType) throws SQLException {
        Connection conn = ps.getConnection();
        Array array = conn.createArrayOf("smallint", parameter);
        ps.setArray(i, array);
    }

    @Override
    public Short[] getNullableResult(ResultSet rs, String columnName) throws SQLException {
        return convertToArray(rs.getArray(columnName));
    }

    @Override
    public Short[] getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return convertToArray(rs.getArray(columnIndex));
    }

    @Override
    public Short[] getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return convertToArray(cs.getArray(columnIndex));
    }

    private Short[] convertToArray(Array value) throws SQLException {
        if (value == null) {
            return null;
        }
        Object valueArray = value.getArray();
        if (valueArray instanceof Integer[]) {
            Integer[] array = (Integer[]) valueArray;
            Short[] shorts = new Short[array.length];
            for (int i = 0; i < array.length; i++) {
                shorts[i] = array[i].shortValue();
            }
            return shorts;
        } else {
            return (Short[]) valueArray;
        }
    }
}
