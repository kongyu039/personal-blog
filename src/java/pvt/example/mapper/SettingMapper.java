package pvt.example.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import pvt.example.pojo.entity.Setting;

import java.util.List;

/**
 * 信息：src/java/pvt/example/mapper/SettingMapper.java
 * <p>日期：2025/9/7
 * <p>描述：
 */
@Mapper
public interface SettingMapper {
    public List<Setting> selectSettingAll();

    public Integer updateSettingByKeyValue(@Param("key") String key, @Param("value") String value);
}
