package pvt.example.common.handler;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import pvt.example.common.config.GlobalVariable;
import pvt.example.common.utils.SystemUtil;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * 信息：src/java/pvt/example/common/handler/TaskGlobalJob.java
 * <p>日期：2025/9/6
 * <p>描述：
 */
@Component
public class TaskGlobalJob {
    @Resource
    private GlobalVariable globalVariable;

    /** 更新cpu信息 */
    private void updateCPUInfo() {
        Map<String, String> cpuInfo = new HashMap<String, String>();
        cpuInfo.put("cpuLoad", SystemUtil.cpuLoad()); // cpu负载
        cpuInfo.put("cpuCore", SystemUtil.cpuCore()); // cpu逻辑核心数
        cpuInfo.put("cpuMaxFreq", SystemUtil.cpuMaxFreq()); // cpu最大频率
        cpuInfo.put("cpuNowFreq", SystemUtil.cpuNowFreq()); // cpu平均频率
        globalVariable.setCpuInfo(cpuInfo);
    }

    /** 更新memory/disk信息 */
    private void updateMemoryDiskInfo() {
        Map<String, String> memoryDiskInfo = new HashMap<String, String>();
        memoryDiskInfo.put("memoryTotal", SystemUtil.memoryTotal()); // 内存总共
        memoryDiskInfo.put("memoryAvailable", SystemUtil.memoryAvailable()); // 内存可用率
        memoryDiskInfo.put("diskTotal", SystemUtil.diskTotal()); // 硬盘总共
        memoryDiskInfo.put("diskAvailable", SystemUtil.diskAvailable()); // 硬盘可用率
        globalVariable.setMemoryDiskInfo(memoryDiskInfo);
    }

    /** 更新服务器系统信息 */
    private void updateOsInfo() {
        Map<String, String> osInfo = new HashMap<String, String>();
        osInfo.put("osName", SystemUtil.osName()); // 服务器名称
        osInfo.put("osType", SystemUtil.osType()); // 服务器操作系统类型
        osInfo.put("osArch", SystemUtil.osArch()); // 服务器系统架构
        osInfo.put("osTime", SystemUtil.osTime()); // 服务器运行时间
        osInfo.put("osVendor", SystemUtil.osVendor()); // 服务器操作系统供应商
        globalVariable.setOsInfo(osInfo);
    }

    /** 更新JVM信息 */
    private void updateJVMInfo() {
        Map<String, String> jvmInfo = new HashMap<String, String>();
        jvmInfo.put("jvmName", SystemUtil.jvmName()); // jvm名称
        jvmInfo.put("jvmVersion", SystemUtil.jvmVersion()); // jvm版本
        jvmInfo.put("jvmVendor", SystemUtil.jvmVendor()); // jvm供应商
        jvmInfo.put("jvmTime", SystemUtil.jvmTime()); // jvm运行时间
        jvmInfo.put("jvmMax", SystemUtil.jvmMax()); // jvm最大内存
        jvmInfo.put("jvmTotal", SystemUtil.jvmTotal()); // jvm当前内存
        jvmInfo.put("jvmFree", SystemUtil.jvmFree()); // jvm可用内存
        jvmInfo.put("jvmHome", SystemUtil.jvmHome()); // jvm的Home路径
        globalVariable.setJvmInfo(jvmInfo);
    }

    @PostConstruct
    private void onStart() {
        // 异步初始化
        CompletableFuture.runAsync(() -> {
            updateCPUInfo();
            updateMemoryDiskInfo();
            updateOsInfo();
            updateJVMInfo();
        });
    }

    @Scheduled(cron = "0 0/15 * * * ?")
    private void schedule() {
        updateCPUInfo();
        updateMemoryDiskInfo();
        updateOsInfo();
        updateJVMInfo();
    }
}
