package pvt.example.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 信息：src/java/pvt/example/service/BasicService2.java
 * <p>日期：2025/7/31
 * <p>描述：
 */
public interface BasicService {
    public Set<String> changeIpHost(String type, String content, String flag);

    public Map<String, String> getIpHost();

    public Map<String, Integer> countTotalAll();

    public List<Map<String, Integer>> countDayCalculate(String year);

    public Map<String, List<Map<String, Integer>>> countCategoryTag();

    public Map<String, Map<String, String>> getSystemInfo();
}
