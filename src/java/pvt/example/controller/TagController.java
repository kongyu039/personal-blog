package pvt.example.controller;

import org.springframework.web.bind.annotation.*;
import pvt.example.pojo.entity.Tag;
import pvt.example.pojo.vo.ResultVO;
import pvt.example.service.TagService;

import javax.annotation.Resource;
import java.util.List;

/**
 * 信息：src/java/pvt/example/controller/TagController.java
 * <p>日期：2025/8/13
 * <p>描述：标签Controller类
 */
@RestController
@RequestMapping("/tag")
public class TagController extends BaseController {
    @Resource
    private TagService tagService;

    @GetMapping("/all-list")
    private ResultVO<List<Tag>> getAllTags() { return successResultVO(tagService.getTags()); }

    @GetMapping("/get-tags")
    private ResultVO<List<Tag>> getTagsByPostId(@RequestParam Long postId) {
        return successResultVO(tagService.getTagsByPostId(postId));
    }

    @PostMapping("/add-tag")
    private ResultVO<Integer> addTag(Tag tag) { return successResultVO(tagService.addTag(tag)); }

    @PostMapping("/del-tag")
    private ResultVO<Integer> deleteTag(@RequestParam Integer[] ids) { return successResultVO(tagService.delTagById(ids)); }

    @PostMapping("/edit-tag")
    private ResultVO<Integer> editTag(Tag tag) { return successResultVO(tagService.editTag(tag)); }
}
