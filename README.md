```java
public static String getJarDirectory() {
    try {
        // 获取JAR包的URI
        String jarPath = YourMainClass.class.getProtectionDomain()
                                            .getCodeSource().getLocation().toURI().getPath();

        // 若是JAR包，则返回其父目录；若是类文件，则返回类路径根目录
        File jarFile = new File(jarPath);
        return jarFile.isFile()
               ? jarFile.getParent()
               : System.getProperty("user.dir");
    } catch (URISyntaxException e) {
        throw new RuntimeException("无法获取JAR包目录", e);
    }
}

// 使用示例
String dbDir = getJarDirectory();
String dbPath = Paths.get(dbDir, "mydatabase.db").toString();
String url = "jdbc:sqlite:" + dbPath;
```

```html
<footer>
    <div>
        <p>© 2023 Your Blog Name. All rights reserved.</p>
        <p>Version 1.0.0</p>
        <p>支持邮箱：<a href="mailto:support@yourblog.com">support@yourblog.com</a></p>
    </div>
    <div>
        <ul>
            <li><a href="/help">帮助文档</a></li>
            <li><a href="/faq">常见问题解答</a></li>
            <li><a href="/contact">联系方式</a></li>
            <li><a href="/privacy">隐私政策</a></li>
            <li><a href="/terms">服务条款</a></li>
        </ul>
    </div>
    <div>
        <p>关注我们：</p>
        <a href="https://twitter.com/yourblog">Twitter</a>
        <a href="https://facebook.com/yourblog">Facebook</a>
        <a href="https://instagram.com/yourblog">Instagram</a>
    </div>
</footer>
```

```java
import java.util.concurrent.atomic.AtomicInteger;

public class SimpleIdGenerator {
    private final int machineId; // 机器 ID
    private final AtomicInteger sequence; // 序列号

    public SimpleIdGenerator(int machineId) {
        this.machineId = machineId; // 设置机器 ID
        this.sequence = new AtomicInteger(0); // 初始化序列号
    }

    public String nextId() {
        // 获取当前秒的时间戳（10位）
        long timestamp = System.currentTimeMillis() / 1000;

        // 获取当前序列号（5位），并确保在 0-99999 之间
        int seq = sequence.getAndIncrement() % 100000;

        // 生成 ID 字符串
        return String.format("%d%d%05d", timestamp, machineId, seq);
    }

    public static void main(String[] args) {
        SimpleIdGenerator generator = new SimpleIdGenerator(1); // 机器 ID 为 1

        // 生成几个 ID 示例
        for (int i = 0; i < 10; i++) {
            String id = generator.nextId();
            System.out.println("Generated ID: " + id);
        }
    }
}
```
```java
package pvt.example.common.utils;

/**
 * 信息：src/java/pvt/example/common/utils/SnowFlakeUtil.java
 * <p>日期：2025/8/3
 * <p>描述：雪花算法 32 位
 */
public class SnowFlakeUtil2{
    //起始时间戳
    private static final long startTimeStamp;
    //机器ID
    private static final long workID;
    //数据中心ID
    private static final long  dataCenterID;
    //序列号
    private static long sequence;
    //数据中心ID移动位数
    private static final long dataCenterIndex;
    //机器ID移动位数
    private static final long workIDIndex;
    //时间戳移动位数
    private static final long timeStampIndex;
    //记录上一次时间戳
    private static long lastTimeStamp;
    //序列号掩码
    private static final long sequenceMask;
    //序列号长度12位
    private static final long sequenceLength;

    //初始化数据
    static {
        startTimeStamp = 1577808000000L;
        //设置机器编号 1
        workID = 1L;
        //设置数据中心ID 1
        dataCenterID = 1L;
        //起始序列号 0开始
        sequence = 0L;
        //数据中心位移位数
        dataCenterIndex = 12L;
        //机器ID位移位数
        workIDIndex = 17L;
        //时间戳位移位数
        timeStampIndex = 22L;
        //记录上次时间戳
        lastTimeStamp = -1L;
        //序列号长度
        sequenceLength = 12L;
        //序列号掩码
        sequenceMask = ~(-1L << sequenceLength);
    }
    public synchronized static Long getID(){
        //获得当前时间
        long now = System.currentTimeMillis();
        //当前系统时间小于上一次记录时间
        if (now < lastTimeStamp){
            throw new RuntimeException("时钟回拨异常");
        }
        //相同时间 要序列号进制增量
        if (now == lastTimeStamp){
            //防止溢出
            sequence = (sequence + 1) & sequenceMask;
            if (sequence == 0L){
                //溢出处理
                try {
                    Thread.sleep(1L);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                //获取下一毫秒时间 （有锁）
                now = System.currentTimeMillis();
            }
        }else{
            //置0 之前序列号同一时间并发后自增 到这里说明时间不同了 版本号所以置0
            sequence = 0L;
        }
        //记录当前时间
        lastTimeStamp = now;
        //当前时间和起始时间插值 右移 22位
        long handleTimeStamp = (now - startTimeStamp) << timeStampIndex;
        // 数据中心数值 右移动 17位 并且 按位或
        long handleWorkID = (dataCenterID << dataCenterIndex) | handleTimeStamp;
        // 机器ID数值 右移动 12位 并且 按位或
        long handleDataCenterID = (workID << workIDIndex) | handleWorkID;
        // 序列号 按位或
        return handleDataCenterID | sequence;
    }
}

```