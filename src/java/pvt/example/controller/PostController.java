package pvt.example.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pvt.example.pojo.entity.Post;
import pvt.example.pojo.query.PostQuery;
import pvt.example.pojo.vo.ResultPageVO;
import pvt.example.pojo.vo.ResultVO;
import pvt.example.service.PostService;

import javax.annotation.Resource;

/**
 * 信息：src/java/pvt/example/controller/PostController.java
 * <p>日期：2025/8/10
 * <p>描述：
 */
@RestController
@RequestMapping("/post")
public class PostController extends BaseController {
    @Resource
    private PostService postService;

    /**
     * 查询分页及基础Post
     * @param postQuery Post参数(表单接收 application/x-www-form-urlencoded )
     */
    @PostMapping("/get-posts")
    private ResultVO<ResultPageVO<Post>> getPostList(PostQuery postQuery) {
        System.out.println("postQuery = " + postQuery);
        if (postQuery.getPage() <= 0) { postQuery.setPage(1); }
        if (postQuery.getLimit() <= 0) { postQuery.setLimit(5); }
        ResultPageVO<Post> resultPosts = postService.queryPost(postQuery);
        return successResultVO(resultPosts);
    }

    /**
     * 上传文章
     * @param post 文章
     */
    private ResultVO<String> putPost(Post post) {
        return successResultVO(null);
    }
}
