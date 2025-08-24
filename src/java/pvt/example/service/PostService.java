package pvt.example.service;

import pvt.example.pojo.dto.PostRequest;
import pvt.example.pojo.entity.Post;
import pvt.example.pojo.query.PostQuery;
import pvt.example.pojo.vo.ResultPageVO;

/**
 * 信息：src/java/pvt/example/service/PostService.java
 * <p>日期：2025/8/9
 * <p>描述：
 */
public interface PostService {
    public ResultPageVO<Post> queryPost(PostQuery postQuery);

    public void createPost(PostRequest postRequest);

    public void deletePost(Long[] ids);

    public Post queryPostById(Long postId);

    public void editPost(PostRequest postRequest);

    public void editPostTop(Long postId, Integer isTop);

    public void editPostDel(Long postId, Integer isDel);
}
