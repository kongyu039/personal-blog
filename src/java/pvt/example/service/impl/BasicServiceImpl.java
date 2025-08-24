package pvt.example.service.impl;

import org.springframework.stereotype.Service;
import pvt.example.mapper.CategoryMapper;
import pvt.example.pojo.entity.Category;
import pvt.example.service.BasicService;

import javax.annotation.Resource;
import java.util.List;

/**
 * 信息：src/java/pvt/example/service/impl/BasicServiceImpl.java
 * <p>日期：2025/8/13
 * <p>描述：分类Category Service服务实现类
 */
@Service
public class BasicServiceImpl implements BasicService {
    @Resource
    private CategoryMapper categoryMapper;

    @Override
    public List<Category> getCategories() {
        return categoryMapper.selectCategories();
    }

    @Override
    public Category getCategoryByPostId(Long postId) {
        return categoryMapper.selectCategoryByPostId(postId);
    }
}
