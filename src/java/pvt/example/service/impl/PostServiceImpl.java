package pvt.example.service.impl;

import org.springframework.stereotype.Service;
import pvt.example.mapper.PostMapper;
import pvt.example.pojo.entity.Post;
import pvt.example.pojo.query.PostQuery;
import pvt.example.pojo.vo.ResultPageVO;
import pvt.example.service.PostService;

import javax.annotation.Resource;
import java.util.List;

/**
 * 信息：src/java/pvt/example/service/impl/PostServiceImpl.java
 * <p>日期：2025/8/9
 * <p>描述：
 */
@Service
public class PostServiceImpl implements PostService {
    @Resource
    private PostMapper postMapper;

    @Override
    public ResultPageVO<Post> queryPost(PostQuery postQuery) {
        ResultPageVO<Post> resultPageVO = new ResultPageVO<>();
        List<Post> posts = postMapper.selectPost(postQuery);
        Integer totalCount = postMapper.selectPostCount(postQuery);
        resultPageVO.setResults(posts);
        resultPageVO.setPageNumber(postQuery.getPage());
        resultPageVO.setPageSize(postQuery.getLimit());
        resultPageVO.setTotalCount(totalCount);
        return resultPageVO;
    }
}
