package pvt.example.service;

import java.util.concurrent.CompletableFuture;

/**
 * 信息：src/java/pvt/example/service/BasicAsyncService.java
 * <p>日期：2025/9/8
 * <p>描述：异步基础服务接口
 */
public interface BasicAsyncService {
    public CompletableFuture<String> asyncMethod();
}
