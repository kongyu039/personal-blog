package pvt.example.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pvt.example.pojo.entity.Category;
import pvt.example.pojo.vo.ResultVO;
import pvt.example.service.BasicService;

import javax.annotation.Resource;
import java.util.List;

/**
 * 信息：src/java/pvt/example/controller/CategoryController.java
 * <p>日期：2025/8/13
 * <p>描述：分类Controller类
 */
@RestController
@RequestMapping("/category")
public class CategoryController extends BaseController {
    @Resource
    private BasicService basicService;

    @GetMapping("/all-list")
    private ResultVO<List<Category>> getAllCategories() { return successResultVO(basicService.getCategories()); }

    @GetMapping("/get-category")
    private ResultVO<Category> getCategoryByPostId(@RequestParam Long postId) {
        return successResultVO(basicService.getCategoryByPostId(postId));
    }
}
