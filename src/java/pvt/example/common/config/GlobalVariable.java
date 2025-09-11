package pvt.example.common.config;

import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 信息：src/java/pvt/example/common/config/GlobalVariable.java
 * <p>日期：2025/9/6
 * <p>描述：全局变量
 */
@Component
public class GlobalVariable {
    /** CPU信息 cpuLoad|cpuCore|cpuMaxFreq|cpuNowFreq */
    private Map<String, String> cpuInfo;
    /** 内存硬盘信息 memoryTotal|memoryAvailable|diskTotal|diskAvailable */
    private Map<String, String> memoryDiskInfo;
    /** 服务器系统信息 osName|osType|osArch|osCompany */
    private Map<String, String> osInfo;
    /** JVM信息 */
    private Map<String, String> jvmInfo;
    /** 生成文件Flag */
    private boolean genFileFlag;

    public Map<String, String> getCpuInfo() { return cpuInfo; }

    public void setCpuInfo(Map<String, String> cpuInfo) { this.cpuInfo = cpuInfo; }

    public Map<String, String> getMemoryDiskInfo() { return memoryDiskInfo; }

    public void setMemoryDiskInfo(Map<String, String> memoryDiskInfo) { this.memoryDiskInfo = memoryDiskInfo; }

    public Map<String, String> getOsInfo() { return osInfo; }

    public void setOsInfo(Map<String, String> osInfo) { this.osInfo = osInfo; }

    public Map<String, String> getJvmInfo() { return jvmInfo; }

    public void setJvmInfo(Map<String, String> jvmInfo) { this.jvmInfo = jvmInfo; }

    public boolean getGenFileFlag() { return genFileFlag; }

    public void setGenFileFlag(boolean genFileFlag) { this.genFileFlag = genFileFlag; }
}
