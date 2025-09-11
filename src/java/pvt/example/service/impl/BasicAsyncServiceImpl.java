package pvt.example.service.impl;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import pvt.example.service.BasicAsyncService;

import java.util.concurrent.CompletableFuture;

/**
 * 信息：src/java/pvt/example/service/impl/BasicAsyncServiceImpl.java
 * <p>日期：2025/9/8
 * <p>描述：基础异步服务实现类 (public可以异步)
 */
@Service
@Async
public class BasicAsyncServiceImpl implements BasicAsyncService {
    @Override
    public CompletableFuture<String> asyncMethod() {
        // 模拟长时间运行的任务
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("true = " + true);
        return CompletableFuture.completedFuture("任务完成");
    }
}
