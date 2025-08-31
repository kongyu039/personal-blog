package pvt.example.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pvt.example.mapper.BridgeMapper;
import pvt.example.mapper.TagMapper;
import pvt.example.pojo.entity.Tag;
import pvt.example.service.TagService;

import javax.annotation.Resource;
import java.util.List;

/**
 * 信息：src/java/pvt/example/service/TagServiceImpl.java
 * <p>日期：2025/8/13
 * <p>描述：标签Tag Service服务实现类
 */
@Service
public class TagServiceImpl implements TagService {
    @Resource
    private TagMapper tagMapper;
    @Resource
    private BridgeMapper bridgeMapper;

    @Override
    public List<Tag> getTags() { return tagMapper.selectTags(); }

    @Override
    public List<Tag> getTagsByPostId(Long postId) { return tagMapper.selectTagsByPostId(postId); }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer delTagById(Integer[] ids) {
        Integer i = tagMapper.deleteTagById(ids);
        Integer j = bridgeMapper.delTagPostByTagId(ids);
        return i + j;
    }

    @Override
    public Integer addTag(Tag tag) {
        tagMapper.insertTag(tag);
        return tag.getId();
    }

    @Override
    public Integer editTag(Tag tag) { return tagMapper.updateTag(tag); }
}
