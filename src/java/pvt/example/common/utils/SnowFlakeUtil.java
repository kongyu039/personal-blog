package pvt.example.common.utils;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 信息：src/java/pvt/example/common/utils/SnowFlakeUtil.java
 * <p>日期：2025/8/12
 * <p>描述：小型雪花ID 14位
 */
public class SnowFlakeUtil {
    private SnowFlakeUtil() { }

    // 秒时间戳1748707200(10位)+机器id(1位)+序列号(2位)+1位校验
    private static final long START_TIMESTAMP = 1748707200L; // 起始时间戳（2025-06-01 00:00:00）
    // 机器ID (0-9)
    private static final int MACHINE_ID = Integer.getInteger("snowflake.machine.id", 0);
    // 序列号 (0-99)
    private static final AtomicInteger sequence = new AtomicInteger(ThreadLocalRandom.current().nextInt(10000));
    // 上次时间戳（使用AtomicLong保证原子性）
    private static final AtomicLong lastTimestamp = new AtomicLong(-1);

    static {
        // 初始化时间戳
        lastTimestamp.set(START_TIMESTAMP);
        if (MACHINE_ID < 0 || MACHINE_ID > 9) {
            throw new IllegalArgumentException("Machine ID must be between 0 and 9");
        }
    }

    /** @return 获取当前时间戳(s) */
    private static long getCurrentTimestamp() { return System.currentTimeMillis() / 1000; }

    /** 生成13位唯一ID（线程安全） */
    public static long nextId() {
        while (true) {
            long currentTimestamp = SnowFlakeUtil.getCurrentTimestamp();
            long oldTimestamp = lastTimestamp.get();
            // 处理时钟回拨
            if (currentTimestamp < oldTimestamp) { throw new RuntimeException("Clock moved backwards"); }
            // 时间戳相同则递增序列号
            if (currentTimestamp == oldTimestamp) {
                int currentSeq = sequence.get();
                int nextSeq = (currentSeq + 1) % 100;
                if (sequence.compareAndSet(currentSeq, nextSeq)) {
                    return assembleId(currentTimestamp, nextSeq);
                }
            } // 新时间戳则重置序列号
            else if (lastTimestamp.compareAndSet(oldTimestamp, currentTimestamp)) {
                int newSeq = ThreadLocalRandom.current().nextInt(100);
                sequence.set(newSeq);
                return assembleId(currentTimestamp, newSeq);
            }
        }
    }

    // 简单校验位计算（模10算法）
    private static int calculateCheckDigit(String number) {
        int sum = 0;
        for (int i = 0; i < number.length(); i++) {
            int digit = Character.getNumericValue(number.charAt(i));
            sum += (i % 2 == 0) ? digit * 2 : digit;
        }
        return (10 - (sum % 10)) % 10;
    }

    /**
     * 拼装13位+1位校验
     * @param timestamp 时间戳(s)
     * @param seq (序列号)
     * @return 13位长+1位校验
     */
    private static long assembleId(long timestamp, int seq) {
        // 10位时间戳（秒）
        String timestampPart = String.format("%010d", timestamp);
        // 1位机器ID
        String machinePart = String.valueOf(MACHINE_ID);
        // 2位序列号
        String seqPart = String.format("%02d", seq);
        // 组合14位
        String id13 = timestampPart + machinePart + seqPart;
        // 计算校验位（第14位）
        int checkDigit = calculateCheckDigit(id13);
        return Long.parseLong(id13 + checkDigit);
    }
}
