package pvt.example.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import pvt.example.pojo.dto.PostRequest;
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

    public Post selectPostByPostId(Long id);

    public Integer insertPost(PostRequest postCreate);

    public Integer deletePost(Long[] ids);

    public Integer updatePost(PostRequest postRequest);

    public Integer updatePostTop(@Param("postId") Long postId, @Param("isTop") Integer isTop);

    public Integer updatePostDel(@Param("postId") Long postId, @Param("isDel")Integer isDel);
}
