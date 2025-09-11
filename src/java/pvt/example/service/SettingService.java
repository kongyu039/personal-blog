package pvt.example.service;

import pvt.example.pojo.entity.Setting;

import java.util.List;

/**
 * 信息：src/java/pvt/example/service/SettingService.java
 * <p>日期：2025/9/7
 * <p>描述：
 */
public interface SettingService {
    public List<Setting> getSettingList();

    public  Integer changeSettingByKeyValue(String key, String value);
}
