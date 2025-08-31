package pvt.example.controller;

import org.springframework.web.bind.annotation.*;
import pvt.example.pojo.dto.PostRequest;
import pvt.example.pojo.entity.Post;
import pvt.example.pojo.query.PostQuery;
import pvt.example.pojo.vo.ResultPageVO;
import pvt.example.pojo.vo.ResultVO;
import pvt.example.service.PostService;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

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
        if (postQuery.getPage() == null || postQuery.getPage() <= 0) { postQuery.setPage(1); }
        if (postQuery.getLimit() == null || postQuery.getLimit() <= 0) { postQuery.setLimit(5); }
        ResultPageVO<Post> resultPosts = postService.queryPost(postQuery);
        return successResultVO(resultPosts);
    }

    /**
     * 通过postId查询信息
     * @param postId 文章id
     */
    @GetMapping("/get-post")
    private ResultVO<Post> getPostById(@RequestParam Long postId) { return successResultVO(postService.queryPostById(postId)); }

    /** 获取回收站文章 */
    @GetMapping("/recycle-posts")
    private ResultVO<List<Post>> recyclePosts() {return successResultVO(postService.recyclePosts());}

    /**
     * 新增上传文章
     * @param postRequest 文章
     */
    @PostMapping("/add-post")
    private ResultVO<String> addPost(@Valid PostRequest postRequest) {
        postService.createPost(postRequest);
        return successResultVO();
    }

    /**
     * 彻底删除文章(支持批量删除)
     * @param ids 文章id数组
     */
    @PostMapping("/del-post")
    private ResultVO<String> delPost(@RequestParam Long[] ids) {
        postService.deletePost(ids);
        return successResultVO();
    }

    /**
     * 修改文章标记(删除/恢复)
     * @param ids 文章id数组
     */
    @PostMapping("/edit-post-del")
    private ResultVO<Integer> editPostDel(@RequestParam Long[] ids, @RequestParam Boolean flag) {
        return successResultVO(postService.editPostDel(ids, flag));
    }

    /**
     * 文章修改
     * @param postRequest 文章
     */
    @PostMapping("/edit-post")
    private ResultVO<String> editPost(@Valid PostRequest postRequest) {
        postService.editPost(postRequest);
        return successResultVO();
    }

    @PostMapping("/edit-post-top")
    private ResultVO<String> editPostTop(@RequestParam Long postId, @RequestParam Integer isTop) {
        postService.editPostTop(postId, isTop);
        return successResultVO(isTop == 0 ? "否" : "是");
    }
}
