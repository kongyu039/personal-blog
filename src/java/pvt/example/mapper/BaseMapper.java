package pvt.example.mapper;

import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Mapper;
import pvt.example.pojo.entity.TagPost;

import java.util.List;
import java.util.Map;

/**
 * 信息：src/java/pvt/example/mapper/BaseMapper2.java
 * <p>日期：2025/7/31
 * <p>描述：基础Dao
 */
@Mapper
public interface BaseMapper {
    public Integer selectAllPostCount();

    public Integer selectAllCategoryCount();

    public Integer selectAllTagCount();

    public Integer selectAllRecycleCount();

    public Integer selectAllImageCount();

    public List<TagPost> selectAllTagPost();

    @MapKey("day_date")
    public List<Map<String, Integer>> selectAllPostDayCount(String year);

    @MapKey("category")
    public List<Map<String, Integer>> selectOrderByCategoryCount();

    @MapKey("tag")
    public List<Map<String, Integer>> selectOrderByTagCount();
}