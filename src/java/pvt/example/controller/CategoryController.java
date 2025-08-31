package pvt.example.controller;

import org.springframework.web.bind.annotation.*;
import pvt.example.pojo.entity.Category;
import pvt.example.pojo.vo.ResultVO;
import pvt.example.service.CategoryService;

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
    private CategoryService categoryService;

    @GetMapping("/all-list")
    private ResultVO<List<Category>> getAllCategories() { return successResultVO(categoryService.getCategories()); }

    @GetMapping("/get-category")
    private ResultVO<Category> getCategoryByPostId(@RequestParam Long postId) {
        return successResultVO(categoryService.getCategoryByPostId(postId));
    }

    @PostMapping("/add-category")
    private ResultVO<Integer> addCategory(Category category) { return successResultVO(categoryService.addCategory(category)); }

    @PostMapping("/del-category")
    private ResultVO<Integer> delCategory(@RequestParam Integer[] ids) { return successResultVO(categoryService.delCategoryById(ids)); }

    @PostMapping("/edit-category")
    private ResultVO<Integer> editCategory(Category category) { return successResultVO(categoryService.editCategory(category)); }
}
