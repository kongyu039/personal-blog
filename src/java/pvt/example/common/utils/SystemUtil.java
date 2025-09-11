package pvt.example.common.utils;

import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;
import oshi.hardware.GlobalMemory;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.software.os.FileSystem;
import oshi.software.os.OSFileStore;
import oshi.software.os.OperatingSystem;
import oshi.util.Util;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Properties;

/**
 * 信息：src/java/pvt/example/common/utils/SystemUtil.java
 * <p>日期：2025/9/5
 * <p>描述：系统工具 基于OSHI库 系统信息[CPU,内存]+JVM信息+服务器信息
 */
public class SystemUtil {
    /** 系统信息 */
    private static final SystemInfo systemInfo = new SystemInfo();
    /** 系统硬件信息 */
    private static final HardwareAbstractionLayer hardware = systemInfo.getHardware();
    /** 硬盘信息 */
    private static final OperatingSystem operatingSystem = systemInfo.getOperatingSystem();
    /** CPU信息 */
    private static final CentralProcessor processor = hardware.getProcessor();
    /** 内存信息 */
    private static final GlobalMemory memory = hardware.getMemory();
    /** 硬盘信息 */
    private static final FileSystem fileSystem = operatingSystem.getFileSystem();
    /** JVM的RuntimeMXBean */
    private static final RuntimeMXBean runtimeMXBean = ManagementFactory.getRuntimeMXBean();
    /** 系统属性 */
    private static final Properties props = System.getProperties();

    /** CPU使用率|负载 */
    public static String cpuLoad() {
        long[] prevTicks = processor.getSystemCpuLoadTicks();
        Util.sleep(1000);
        processor.getSystemCpuLoadTicks();
        double d = processor.getSystemCpuLoadBetweenTicks(prevTicks) * 100;
        return Math.round(d * 100) / 100.0 + "%";
    }

    /** CPU逻辑核心数 */
    public static String cpuCore() { return String.valueOf(processor.getPhysicalProcessorCount()); }

    /** CPU最大频率Ghz */
    public static String cpuMaxFreq() { return String.format("%.2fGhz", processor.getMaxFreq() / 1_000_000_000.0); }

    /** CPU当前频率Ghz */
    public static String cpuNowFreq() {
        long[] currentFreqArray = processor.getCurrentFreq();
        long currentAvg = 0;
        if (currentFreqArray.length > 0) {
            for (long f : currentFreqArray) { currentAvg += f; }
            currentAvg /= currentFreqArray.length;
        }
        return String.format("%.2fGhz", currentAvg / 1_000_000_000.0);
    }

    /** 内存总大小 */
    public static String memoryTotal() { return String.format("%.2fGB", memory.getTotal() / 1024.0 / 1024 / 1024); }

    /** 内存可用使用率 */
    public static String memoryAvailable() { return String.format("%.2f%%", 100.0 * memory.getAvailable() / memory.getTotal()); }

    /** 硬盘总大小 */
    public static String diskTotal() {
        long total = 0;
        for (OSFileStore store : fileSystem.getFileStores()) { total += store.getTotalSpace(); }
        return String.format("%.2fGB", total / 1024.0 / 1024 / 1024);
    }

    /** 硬盘可用使用率 */
    public static String diskAvailable() {
        long total = 0, usable = 0;
        for (OSFileStore store : fileSystem.getFileStores()) {
            total += store.getTotalSpace();
            usable += store.getUsableSpace(); // 可用空间
        }
        return String.format("%.2f%%", 100.0 * usable / total);
    }

    /** 服务器名称 */
    public static String osName() {
        try { return InetAddress.getLocalHost().getHostName(); } catch (UnknownHostException e) { throw new RuntimeException(e); }
    }

    /** 服务器操作系统类型 */
    public static String osType() { return System.getProperty("os.name"); }

    /** 服务器系统架构 */
    public static String osArch() { return System.getProperty("os.arch"); }

    /** 服务器操作系统供应商 */
    public static String osVendor() { return operatingSystem.getManufacturer(); }

    /** 服务器开机运行时间 */
    public static String osTime() {
        long currentTimeMs = System.currentTimeMillis() / 1000;
        long bootTimeMs = operatingSystem.getSystemBootTime();
        return AppUtil.secondToDate(currentTimeMs - bootTimeMs);
    }

    /** JVM名称 */
    public static String jvmName() { return runtimeMXBean.getVmName(); }

    /** JVM版本 */
    public static String jvmVersion() { return runtimeMXBean.getVmVersion(); }

    /** JVM供应商 */
    public static String jvmVendor() { return runtimeMXBean.getVmVendor(); }

    /** JVM运行时间 */
    public static String jvmTime() { return AppUtil.secondToDate((System.currentTimeMillis() - runtimeMXBean.getStartTime()) / 1000); }

    /** JVM最大内存 */
    public static String jvmMax() { return String.format("%.2fMB", Runtime.getRuntime().maxMemory() / 1024.0 / 1024); }

    /** JVM当前内存 */
    public static String jvmTotal() { return String.format("%.2fMB", Runtime.getRuntime().totalMemory() / 1024.0 / 1024); }

    /** JVM可用内存 */
    public static String jvmFree() { return String.format("%.2fMB", Runtime.getRuntime().freeMemory() / 1024.0 / 1024); }

    /** JVM的Home路径 */
    public static String jvmHome() { return props.getProperty("java.home"); }
}
