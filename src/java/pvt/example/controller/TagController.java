package pvt.example.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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

        @GetMapping("/list")
        private ResultVO<List<Tag>> getAllTags() {
            return successResultVO(tagService.getTags());
        }
}
