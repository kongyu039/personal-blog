package pvt.example.service.impl;

import org.springframework.stereotype.Service;
import pvt.example.mapper.SettingMapper;
import pvt.example.pojo.entity.Setting;
import pvt.example.service.SettingService;

import javax.annotation.Resource;
import java.util.List;

/**
 * 信息：src/java/pvt/example/service/impl/SettingServiceImpl.java
 * <p>日期：2025/9/7
 * <p>描述：
 */
@Service
public class SettingServiceImpl implements SettingService {
    @Resource
    private SettingMapper settingMapper;

    @Override
    public List<Setting> getSettingList() { return settingMapper.selectSettingAll(); }

    @Override
    public Integer changeSettingByKeyValue(String key, String value) {return settingMapper.updateSettingByKeyValue(key,value);}
}
