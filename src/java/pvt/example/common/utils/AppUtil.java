package pvt.example.common.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.system.ApplicationHome;
import org.springframework.util.FileSystemUtils;

import java.io.*;
import java.net.InetAddress;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * 信息：src/java/pvt/example/common/utils/AppUtil.java
 * <p>日期：2025/7/27
 * <p>描述：应用常用工具类
 */
public class AppUtil {
    private static final Logger logger = LoggerFactory.getLogger(AppUtil.class);
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");

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

    /**
     * 获取当前应用程序的运行目录(带有/)。
     * @param dirPath 不定路径
     * @return 应用程序的运行目录路径
     */
    public static Path getJarDirectory(String... dirPath) { return Paths.get(AppUtil.getJarDirectory(), dirPath); }

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
            if (address.isLoopbackAddress()) { return true; }
            // 获取IP地址的字节数组
            byte[] ipBytes = address.getAddress();
            // 判断是否在 10.x.x.x 范围
            if (ipBytes[0] == 10) { return true; }
            // 判断是否在 172.16.x.x 到 172.31.x.x 范围
            if (ipBytes[0] == (byte) 172 && (ipBytes[1] >= 16 && ipBytes[1] <= 31)) { return true; }
            // 判断是否在 192.168.x.x 范围
            return ipBytes[0] == (byte) 192 && ipBytes[1] == (byte) 168;
            // 其他情况不是内网IP
        } catch (UnknownHostException e) {
            return false; // 如果 IP 地址无效，返回 false
        }
    }

    /**
     * 计算文件的MD5校验值
     * @param fis 文件
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
     * @param fis 文件
     * @return MD5值
     */
    public static String calculateCheckMD5(InputStream fis) { return calculateCheckMD5(fis, false); }

    /**
     * 当前日期格式化字符串
     * @param pattern 模式
     * @return 日期格式
     */
    public static String nowDayFormatter(String pattern) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        return LocalDate.now().format(formatter);
    }

    /**
     * 当前日期格式化字符串(yyyy/MM/dd)
     * @return 日期格式
     */
    public static String nowDayFormatter() { return nowDayFormatter("yyyy/MM/dd"); }

    /**
     * 毫秒格式化yyyy/MM/dd
     * @param timestamp 毫秒
     */
    public static String msToFormatter(Long timestamp) { return sdf.format(new Date(timestamp)); }

    /**
     * 日期格式化yyyy/MM/dd
     * @param date 日期
     */
    public static String dateToFormatter(Date date) { return sdf.format(date); }

    /**
     * 秒转日期 N天N时N分
     * @param second 秒
     */
    public static String secondToDate(Long second) {
        String strTime;
        long days = second / (60 * 60 * 24);
        long hours = (second % (60 * 60 * 24)) / (60 * 60);
        long minutes = (second % (60 * 60)) / 60;
        long seconds = second % 60;
        if (days > 0) {
            strTime = days + "天" + hours + "小时" + minutes + "分钟";
        } else if (hours > 0) {
            strTime = hours + "小时" + minutes + "分钟";
        } else if (minutes > 0) {
            strTime = minutes + "分钟" + seconds + "秒";
        } else {
            strTime = second + "秒";
        }
        return strTime;
    }

    /**
     * 删除目录
     * @param path 路径
     */
    public static boolean delFileDir(Path path) {
        try {
            if (Files.exists(path)) {
                Files.walkFileTree(path, new SimpleFileVisitor<Path>() {
                    // 先去遍历删除文件
                    @Override
                    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                        file.toFile().setWritable(true);
                        Files.delete(file);
                        return FileVisitResult.CONTINUE;
                    }

                    // 再去遍历删除目录
                    @Override
                    public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                        dir.toFile().setWritable(true);
                        Files.delete(dir);
                        return FileVisitResult.CONTINUE;
                    }
                });
            }
            return true;
        } catch (IOException e) {
            logger.error("Failed to delete: " + path + " due to: " + e.getMessage(), e);
            logger.debug(e.getMessage());
            return false;
        }
    }

    /**
     * 删除目录
     * @param path 路径
     */
    public static boolean delFileDir(String path) { return delFileDir(Paths.get(path)); }

    /** 拆分List为多个子List */
    public static <T> List<List<T>> splitList(List<T> list, int batchSize) {
        List<List<T>> result = new ArrayList<>();
        if (list == null || list.isEmpty()) {
            return result;
        }

        int total = list.size();
        int batches = (total + batchSize - 1) / batchSize; // 计算总批次

        for (int i = 0; i < batches; i++) {
            int start = i * batchSize;
            int end = Math.min(start + batchSize, total);
            result.add(list.subList(start, end));
        }
        return result;
    }

    /**
     * 使用Spring提供的工具类简化实现（推荐）
     * @param sourceDir 源文件夹路径
     * @param targetDir 目标文件夹路径
     * @return 是否复制成功
     */
    public static boolean copyDirWithSpringUtil(Path sourceDir, Path targetDir) {
        try {
            FileSystemUtils.copyRecursively(sourceDir, targetDir);
            return true;
        } catch (IOException e) {
            logger.debug(e.getMessage());
            return false;
        }
    }

    /**
     * 私有 公共方法：用于将目录中的文件压缩到ZIP输出流中
     * @param sourceDirPath 目录地址
     * @param zipOutputStream zip文件输出流
     */
    private static void zipFiles(Path sourceDirPath, ZipOutputStream zipOutputStream) throws IOException {
        // 手动创建流并在完成后关闭
        try (Stream<Path> paths = Files.walk(sourceDirPath)) {
            paths.filter(path -> !Files.isDirectory(path))
                 .forEach(path -> {
                     ZipEntry zipEntry = new ZipEntry(sourceDirPath.relativize(path).toString());
                     try {
                         zipOutputStream.putNextEntry(zipEntry);
                         Files.copy(path, zipOutputStream);
                         zipOutputStream.closeEntry();
                     } catch (IOException e) {
                         System.err.println("Error while zipping: " + e.getMessage());
                     }
                 });
        }
    }

    /**
     * 目录打包为 Zip 文件 第一个 zipDirectory 方法，返回 zip 文件
     * @param sourceDirPath 目录地址
     * @param zipFilePath zip文件地址
     */
    public static File zipDirectory(Path sourceDirPath, Path zipFilePath) throws IOException {
        try (ZipOutputStream zipOutputStream = new ZipOutputStream(new FileOutputStream(zipFilePath.toString()))) {
            zipFiles(sourceDirPath, zipOutputStream);
            return zipFilePath.toFile();
        }
    }

    /**
     * 目录打包为 byte数组 第二个 zipDirectory 方法，返回 byte 数组
     * @param sourceDirPath 源路径
     * @return byte数组
     */
    public static byte[] zipDirectory(Path sourceDirPath) throws IOException {
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
             ZipOutputStream zipOutputStream = new ZipOutputStream(byteArrayOutputStream)) {
            zipFiles(sourceDirPath, zipOutputStream);
            zipOutputStream.finish();
            return byteArrayOutputStream.toByteArray();
        }
    }
}
