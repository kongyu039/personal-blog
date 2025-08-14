package pvt.example.mapper;

import org.apache.ibatis.annotations.Mapper;
import pvt.example.pojo.entity.Post;
import pvt.example.pojo.query.PostQuery;

import java.util.List;

/**
 * 信息：src/java/pvt/example/mapper/PostMapper.java
 * <p>日期：2025/8/9
 * <p>描述：Post文章Dao
 */
@Mapper
public interface PostMapper {
    public List<Post> selectPost(PostQuery postQuery);

    public Integer selectPostCount(PostQuery postQuery);
}
