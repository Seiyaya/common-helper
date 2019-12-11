package xyz.seiyaya.mybatis.handler;

import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;
import org.apache.ibatis.type.TypeHandler;

import java.sql.*;

/**
 * mybatis存储array类型时直接将array转换成逗号拼接的内容
 * @author wangjia
 * @version 1.0
 * @date 2019/12/11 9:35
 */
@MappedTypes({String[].class})
@MappedJdbcTypes({JdbcType.VARCHAR})
public class StringArrayTypeHandler implements TypeHandler<String[]> {
    @Override
    public void setParameter(PreparedStatement ps, int i, String[] parameter, JdbcType jdbcType) throws SQLException {
        if(parameter == null){
            ps.setNull(i, Types.VARCHAR);
        }else{
            String result = String.join(",", parameter);
            ps.setString(i,result);
        }
    }

    @Override
    public String[] getResult(ResultSet rs, String columnName) throws SQLException {
        String columnValue = rs.getString(columnName);
        return this.getStringArray(columnValue);
    }

    /**
     * 将数据库存储的字符串转换为数组
     * @param columnValue
     * @return
     */
    private String[] getStringArray(String columnValue) {
        if( columnValue == null){
            return null;
        }
        return columnValue.split(",");
    }

    @Override
    public String[] getResult(ResultSet rs, int columnIndex) throws SQLException {
        String columnValue = rs.getString(columnIndex);
        return this.getStringArray(columnValue);
    }

    @Override
    public String[] getResult(CallableStatement cs, int columnIndex) throws SQLException {
        String columnValue = cs.getString(columnIndex);
        return this.getStringArray(columnValue);
    }
}
