package pvt.example.mapper2;

import org.apache.ibatis.annotations.Mapper;
import pvt.example.pojo.entity.Category;
import pvt.example.pojo.entity.Post;
import pvt.example.pojo.entity.Tag;
import pvt.example.pojo.entity.TagPost;

import java.util.List;

/**
 * 信息：src/java/pvt/example/mapper2/BaseMapper2.java
 * <p>日期：2025/9/10
 * <p>描述：
 */
@Mapper
public interface BaseMapper2 {
    public Integer genDataBase();

    public Integer createPostTable();

    public Integer createCategoryTable();

    public Integer createTagTable();

    public Integer createTagPostTable();

    /**
     * 批量插入Post
     * @param posts 文章列表
     */
    public Integer batchInsertPost(List<Post> posts);

    /**
     * 批量插入 Category 分类
     * @param category 分类
     */
    public Integer batchInsertCategory(List<Category> category);
    /**
     * 批量插入 Tag 标签
     * @param tagList 分类
     */
    public Integer batchInsertTag(List<Tag> tagList);
    /**
     * 批量插入 TagPost 中间表
     * @param tagPostList 分类
     */
    public Integer batchInsertTagPost(List<TagPost> tagPostList);
}
