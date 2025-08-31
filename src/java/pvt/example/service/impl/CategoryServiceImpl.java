package pvt.example.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pvt.example.mapper.BridgeMapper;
import pvt.example.mapper.CategoryMapper;
import pvt.example.pojo.entity.Category;
import pvt.example.service.CategoryService;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;

/**
 * 信息：src/java/pvt/example/service/impl/CategoryServiceImpl.java
 * <p>日期：2025/8/13
 * <p>描述：分类Category Service服务实现类
 */
@Service
public class CategoryServiceImpl implements CategoryService {
    @Resource
    private CategoryMapper categoryMapper;
    @Resource
    private BridgeMapper bridgeMapper;

    @Override
    public List<Category> getCategories() { return categoryMapper.selectCategories(); }

    @Override
    public Category getCategoryByPostId(Long postId) { return categoryMapper.selectCategoryByPostId(postId); }

    @Override
    public Integer addCategory(Category category) {
        Integer i = categoryMapper.insertCategory(category);
        return category.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer delCategoryById(Integer[] ids) {
        // 去除 0 保留默认
        ids = Arrays.stream(ids).filter(id -> id != 0).toArray(Integer[]::new);
        if (ids.length > 0) {
            // 按id删除分类
            Integer i = categoryMapper.deleteCategoryById(ids);
            // 此分类删除后,旧文章需要全部变为 0 分类类型
            Integer j = bridgeMapper.uploadCategoryPostByCategoryIdToZero(ids);
            return i + j;
        } else {
            return 0;
        }
    }

    @Override
    public Integer editCategory(Category category) { return categoryMapper.updateCategory(category); }
}
