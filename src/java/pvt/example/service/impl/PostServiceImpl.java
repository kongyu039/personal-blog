package pvt.example.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pvt.example.common.utils.SnowFlakeUtil;
import pvt.example.mapper.BridgeMapper;
import pvt.example.mapper.PostMapper;
import pvt.example.pojo.dto.PostRequest;
import pvt.example.pojo.entity.Post;
import pvt.example.pojo.query.PostQuery;
import pvt.example.pojo.vo.ResultPageVO;
import pvt.example.service.PostService;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * 信息：src/java/pvt/example/service/impl/PostServiceImpl.java
 * <p>日期：2025/8/9
 * <p>描述：Post文章 Service服务实现类
 */
@Service
public class PostServiceImpl implements PostService {
    @Resource
    private PostMapper postMapper;

    @Resource
    private BridgeMapper bridgeMapper;

    /**
     * 查询Post
     * @param postQuery post查询参数
     */
    @Override
    public ResultPageVO<Post> queryPost(PostQuery postQuery) {
        ResultPageVO<Post> resultPageVO = new ResultPageVO<>();
        if (postQuery.getTitle() != null) {
            postQuery.setTitleQuery(String.join("%", Arrays.asList(postQuery.getTitle().split(""))));
        }
        if (postQuery.getSummary() != null) {
            postQuery.setSummaryQuery(String.join("%", Arrays.asList(postQuery.getSummary().split(""))));
        }
        List<Post> posts = postMapper.selectPost(postQuery);
        Integer totalCount = postMapper.selectPostCount(postQuery);
        resultPageVO.setResults(posts);
        resultPageVO.setPageNumber(postQuery.getPage());
        resultPageVO.setPageSize(postQuery.getLimit());
        resultPageVO.setTotalCount(totalCount);
        return resultPageVO;
    }

    /**
     * 通过文章id查询
     * @param postId 文章id
     * @return 文章
     */
    @Override
    public Post queryPostById(Long postId) { return postMapper.selectPostByPostId(postId); }

    /**
     * 创建post文章
     * @param postRequest post文章传输
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createPost(PostRequest postRequest) {
        // 设置 雪花Id 14 ; 完善 postCreate
        postRequest.setId(SnowFlakeUtil.nextId());
        Date currDate = new Date();
        postRequest.setCreatedAt(currDate);
        postRequest.setUpdatedAt(currDate);
        postMapper.insertPost(postRequest);
        bridgeMapper.insertCategoryPost(postRequest);
        if (postRequest.getTagIds().length > 0) {
            bridgeMapper.insertTagPost(postRequest);
        }
    }

    /**
     * 删除post文章 通过id 默认支持批量删除
     * @param ids 文章id
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deletePost(Long[] ids) {
        if (ids.length > 0) {
            postMapper.deletePost(ids);
            bridgeMapper.delCategoryPostByPostId(ids);
            bridgeMapper.delTagPostByPostId(ids);
        }
    }

    /**
     * 修改post文章
     * @param postRequest post文章传输
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void editPost(PostRequest postRequest) {
        Long[] postIds = {postRequest.getId()};
        postRequest.setUpdatedAt(new Date());
        // 更新旧内容
        postMapper.updatePost(postRequest);
        // 先删除旧的,再添加新的
        bridgeMapper.delCategoryPostByPostId(postIds);
        bridgeMapper.insertCategoryPost(postRequest);
        bridgeMapper.delTagPostByPostId(postIds);
        if (postRequest.getTagIds().length > 0) {
            bridgeMapper.insertTagPost(postRequest);
        }
    }

    /**
     * 修改post文章置顶
     * @param postId 文章id
     * @param isTop 置顶状态
     */
    @Override
    public void editPostTop(Long postId, Integer isTop) {
        postMapper.updatePostTop(postId, isTop);
    }

    @Override
    public Integer editPostDel(Long[] ids, Boolean flag) {
        if (ids != null && ids.length > 0) { return postMapper.updatePostDel(ids, flag ? 1 : 0); }
        return 0;
    }

    @Override
    public List<Post> recyclePosts() { return postMapper.selectRecyclePosts(); }
}
