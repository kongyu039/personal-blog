package pvt.example.common.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.system.ApplicationHome;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * 信息：src/java/pvt/example/common/utils/AppUtil.java
 * <p>日期：2025/7/27
 * <p>描述：应用常用工具类
 */
public class AppUtil {
    private static final Logger logger = LoggerFactory.getLogger(AppUtil.class);

    /** 私有化工具构造函数，防止实例化 */
    private AppUtil() { }

    /**
     * 获取当前应用程序的运行目录(带有/)。
     * <p>
     * 当应用程序以 JAR 包形式运行时，返回 JAR 文件所在的目录；
     * 当应用程序以类文件形式运行时（例如从 IDE 中直接运行），返回用户的工作目录。
     * @return 应用程序的运行目录路径，以字符串形式返回
     */
    public static String getJarDirectory() {
        try {
            String jarPath = AppUtil.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();
            File jarFile = new File(jarPath);
            return jarFile.isFile() ? jarFile.getParent() + File.separator : System.getProperty("user.dir") + File.separator;
        } catch (URISyntaxException e) {
            throw new RuntimeException("无法获取JAR包目录", e);
        }
    }

    /** 获取target的目录或者jar包的位置 */
    public static String getLocalDirectory() {
        ApplicationHome home = new ApplicationHome(AppUtil.class);
        File jarF = home.getSource();
        return new File(jarF.getParent() + File.separator).getAbsolutePath() + File.separator;
    }

    /**
     * 获取当前日期的路径字符串，格式为 "yyyy/MM/dd"
     * @return 当前日期的路径字符串
     */
    public static String currentDatePath() {
        LocalDate currentDate = LocalDate.now(); // 获取当前日期
        // 格式化日期为目录结构
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy" + File.separator + "MM" + File.separator + "dd");
        return currentDate.format(formatter);
    }

    /**
     * 判断字符串是否为空或 null
     * @param str 要检查的字符串，允许为 {@code null}
     * @return 如果字符串为 {@code null} 或长度为 0，返回 {@code true}；否则返回 {@code false}
     * @see String#isEmpty()
     */
    public static boolean isEmpty(String str) { return str == null || str.isEmpty() || str.trim().isEmpty(); }

    /**
     * 判断是否为内网IP
     * @param ip 要判断的IP地址
     * @return true 如果是内网IP; false 如果不是内网IP
     */
    public static boolean isPrivateIP(String ip) {
        try {
            InetAddress address = InetAddress.getByName(ip);

            // 检查回环地址
            if (address.isLoopbackAddress()) {
                return true;
            }

            // 获取IP地址的字节数组
            byte[] ipBytes = address.getAddress();

            // 判断是否在 10.x.x.x 范围
            if (ipBytes[0] == 10) {
                return true;
            }

            // 判断是否在 172.16.x.x 到 172.31.x.x 范围
            if (ipBytes[0] == (byte) 172 && (ipBytes[1] >= 16 && ipBytes[1] <= 31)) {
                return true;
            }

            // 判断是否在 192.168.x.x 范围
            return ipBytes[0] == (byte) 192 && ipBytes[1] == (byte) 168;
            // 其他情况不是内网IP
        } catch (UnknownHostException e) {
            // 如果 IP 地址无效，返回 false
            return false;
        }
    }

    /**
     * 计算文件的MD5校验值
     * @param file 文件
     * @param isCapital 是否大写
     * @return MD5值
     */
    public static String calculateCheckMD5(InputStream fis, boolean isCapital) {
        try (fis) {
            // 获取请求的摘要算法实例
            MessageDigest digest = MessageDigest.getInstance("MD5");
            byte[] byteArray = new byte[1024];
            int bytesCount;
            // 读取文件内容并更新摘要
            while ((bytesCount = fis.read(byteArray)) != -1) { digest.update(byteArray, 0, bytesCount); }
            // 获取最终的摘要字节数组
            byte[] bytes = digest.digest();
            StringBuilder sb = new StringBuilder();
            // 将字节数组转换为十六进制字符串
            for (byte b : bytes) { sb.append(String.format("%02x", b)); }
            return isCapital ? sb.toString().toUpperCase() : sb.toString();
        } catch (IOException | NoSuchAlgorithmException e) {
            logger.error("MD5计算错误", e);
            return null;
        }
    }

    /**
     * 计算文件的MD5校验值(默认小写)
     * @param file 文件
     * @return MD5值
     */
    public static String calculateCheckMD5(InputStream fis) { return calculateCheckMD5(fis, false); }

    /**
     * 当前日期格式化字符串
     * @param pattern 模式
     * @return 日期格式
     */
    public static String toDayFormatter(String pattern) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        return LocalDate.now().format(formatter);
    }

    /**
     * 当前日期格式化字符串(yyyy/MM/dd)
     * @return 日期格式
     */
    public static String toDayFormatter() { return toDayFormatter("yyyy/MM/dd"); }
}
