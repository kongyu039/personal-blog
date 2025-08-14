package pvt.example.mapper;

import org.apache.ibatis.annotations.Mapper;

/**
 * 信息：src/java/pvt/example/mapper/BaseMapper.java
 * <p>日期：2025/7/31
 * <p>描述：测试Dao
 */
@Mapper
public interface BaseMapper {
    public void createTable();
}