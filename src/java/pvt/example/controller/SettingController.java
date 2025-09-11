package pvt.example.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pvt.example.pojo.entity.Setting;
import pvt.example.pojo.vo.ResultVO;
import pvt.example.service.SettingService;

import javax.annotation.Resource;
import java.util.List;

/**
 * 信息：src/java/pvt/example/controller/SettingController.java
 * <p>日期：2025/9/7
 * <p>描述：设置控制面板
 */
@RestController
@RequestMapping("/setting")
public class SettingController extends BaseController {
    @Resource
    private SettingService settingService;

    /** 设置list表 */
    @GetMapping("/setting-list")
    public ResultVO<List<Setting>> getSettingList() {
        return successResultVO(settingService.getSettingList());
    }

    /**
     * 修改Setting通过Key:Value
     * @param key 键
     * @param value 值
     */
    @PostMapping("/setting-change")
    public ResultVO<Integer> changeSetting(String key, String value) {
        return successResultVO(settingService.changeSettingByKeyValue(key,value));
    }
}
