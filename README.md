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