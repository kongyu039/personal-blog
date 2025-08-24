package pvt.example.service;

import pvt.example.pojo.entity.Category;

import java.util.List;

/**
 * 信息：src/java/pvt/example/service/BasicService.java
 * <p>日期：2025/8/13
 * <p>描述：
 */
public interface BasicService {
    public List<Category> getCategories();

    public  Category getCategoryByPostId(Long postId);
}
