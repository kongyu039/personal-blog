package pvt.example.common.handler;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

/**
 * 信息：src/java/pvt/example/common/handler/DateTypeHandler.java
 * <p>日期：2025/8/1
 * <p>描述：Mybatis自定义日期处理工具
 */
public class DateTypeHandler extends BaseTypeHandler<Date> {
    //将java类型 转换成 数据库需要的类型
    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, Date parameter, JdbcType jdbcType) throws SQLException {
        long time = parameter.getTime();
        ps.setLong(i,time);
    }

    //将数据库中类型 转换成java类型
    //String参数  要转换的字段名称
    //ResultSet 查询出的结果集
    @Override
    public Date getNullableResult(ResultSet rs, String columnName) throws SQLException {
        //获得结果集中需要的数据(long) 转换成Date类型 返回
        long aLong = rs.getLong(columnName);
        return new Date(aLong);
    }

    //将数据库中类型 转换成java类型
    @Override
    public Date getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        long aLong = rs.getLong(columnIndex);
        return new Date(aLong);
    }

    //将数据库中类型 转换成java类型
    @Override
    public Date getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        long aLong = cs.getLong(columnIndex);
        return new Date(aLong);
    }
}
