package pvt.example.mapper;

import org.apache.ibatis.annotations.Mapper;
import pvt.example.pojo.entity.Tag;

import java.util.List;

/**
 * 信息：src/java/pvt/example/mapper/TagMapper.java
 * <p>日期：2025/8/13
 * <p>描述：
 */
@Mapper
public interface TagMapper {
    public List<Tag> selectTags();

    public List<Tag> selectTagsByPostId(Long postId);

    public Integer deleteTagById(Integer[] ids);

    public Integer insertTag(Tag tag);

    public Integer updateTag(Tag tag);
}
