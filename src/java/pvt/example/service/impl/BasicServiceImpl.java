package pvt.example.service.impl;

import org.springframework.stereotype.Service;
import pvt.example.common.config.GlobalVariable;
import pvt.example.common.config.HostIpConfig;
import pvt.example.mapper.BaseMapper;
import pvt.example.service.BasicService;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 信息：src/java/pvt/example/service/impl/BasicServiceImpl.java
 * <p>日期：2025/7/31
 * <p>描述：通用Service服务实现类
 */
@Service
public class BasicServiceImpl implements BasicService {
    @Resource
    private HostIpConfig hostIpConfig;
    @Resource
    private BaseMapper baseMapper;
    @Resource
    private GlobalVariable globalVariable;

    /**
     * 修改白名单Ip和Host
     * @param type 类型 ip/host
     * @param content 内容
     * @param flag 标记 add/del
     * @return 白名单Set集合
     */
    @Override
    public Set<String> changeIpHost(String type, String content, String flag) {
        if (content.isEmpty()) { return "ip".equalsIgnoreCase(type) ? hostIpConfig.getAllowedIPs() : hostIpConfig.getAllowedHosts(); }
        if ("ip".equalsIgnoreCase(type)) {
            if ("add".equalsIgnoreCase(flag)) {
                hostIpConfig.addIp(content);
            } else {
                hostIpConfig.removeIp(content);
            }
            return hostIpConfig.getAllowedIPs();
        } else {
            if ("add".equalsIgnoreCase(flag)) {
                hostIpConfig.addHost(content);
            } else {
                hostIpConfig.removeHost(content);
            }
            return hostIpConfig.getAllowedHosts();
        }
    }

    /** 获取白名单ips和hosts */
    @Override
    public Map<String, String> getIpHost() {
        Set<String> allowedIPs = hostIpConfig.getAllowedIPs();
        Set<String> allowedHosts = hostIpConfig.getAllowedHosts();
        Map<String, String> result = new HashMap<>();
        result.put("ips", String.join(",", allowedIPs));
        result.put("hosts", String.join(",", allowedHosts));
        return result;
    }

    /** 总数 数据 */
    @Override
    public Map<String, Integer> countTotalAll() {
        HashMap<String, Integer> stringIntegerHashMap = new HashMap<>();
        stringIntegerHashMap.put("post", baseMapper.selectAllPostCount());
        stringIntegerHashMap.put("category", baseMapper.selectAllCategoryCount());
        stringIntegerHashMap.put("tag", baseMapper.selectAllTagCount());
        stringIntegerHashMap.put("recycle", baseMapper.selectAllRecycleCount());
        stringIntegerHashMap.put("image", baseMapper.selectAllImageCount());
        return stringIntegerHashMap;
    }

    /**
     * eCharts 日历热力图 数据
     * @param year 年
     */
    @Override
    public List<Map<String, Integer>> countDayCalculate(String year) { return baseMapper.selectAllPostDayCount(year); }

    /**
     * eCharts 饼图渲染分类和标签 数据
     */
    @Override
    public Map<String, List<Map<String, Integer>>> countCategoryTag() {
        Map<String, List<Map<String, Integer>>> stringIntegerHashMap = new HashMap<>();
        stringIntegerHashMap.put("category", baseMapper.selectOrderByCategoryCount());
        stringIntegerHashMap.put("tag", baseMapper.selectOrderByTagCount());
        return stringIntegerHashMap;
    }

    /** 获取系统信息 */
    @Override
    public Map<String, Map<String, String>> getSystemInfo() {
        Map<String, Map<String, String>> systemInfo = new HashMap<String, Map<String, String>>();
        systemInfo.put("cpu", globalVariable.getCpuInfo());
        systemInfo.put("memoryDisk", globalVariable.getMemoryDiskInfo());
        systemInfo.put("os", globalVariable.getOsInfo());
        systemInfo.put("jvm", globalVariable.getJvmInfo());
        return systemInfo;
    }
}
