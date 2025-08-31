package pvt.example.mapper;

import org.apache.ibatis.annotations.Mapper;
import pvt.example.pojo.entity.Category;

import java.util.List;

/**
 * 信息：src/java/pvt/example/mapper/CategoryMapper.java
 * <p>日期：2025/8/13
 * <p>描述：分类CategoryMapper接口
 */
@Mapper
public interface CategoryMapper {
    public List<Category> selectCategories();

    public Category selectCategoryByPostId(Long postId);

    public Integer insertCategory(Category category);

    public Integer deleteCategoryById(Integer[] ids);

    public Integer updateCategory(Category category);
}
