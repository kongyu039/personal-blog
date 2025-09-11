package pvt.example.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pvt.example.pojo.entity.ImageStorage;
import pvt.example.pojo.vo.ResultVO;
import pvt.example.service.ImageService;

import javax.annotation.Resource;
import java.util.List;

/**
 * 信息：src/java/pvt/example/controller/ImageController.java
 * <p>日期：2025/9/1
 * <p>描述：
 */
@RestController
@RequestMapping("/image")
public class ImageController extends BaseController {
    @Resource
    private ImageService imageService;

    /**
     * 获取所有图片信息
     * @param flag 标记 0为本地 1为远程
     */
    @GetMapping("/list-image")
    private ResultVO<List<ImageStorage>> getAllImages(Integer flag) { return successResultVO(imageService.getAllImages(flag)); }

    @PostMapping("/del-image")
    private ResultVO<Integer> delImage(Integer imageId) { return successResultVO(imageService.deleteImage(imageId)); }

    @PostMapping("/upload")
    public ResultVO<String> upload(@RequestParam("image") MultipartFile imageFile) {
        if (imageFile.isEmpty()) { return successResultVO("Empty"); }
        return successResultVO(imageService.upload(imageFile));
    }

    @PostMapping("/r2upload")
    public ResultVO<String> r2upload(@RequestParam("image") MultipartFile imageFile) {
        if (imageFile.isEmpty()) { return successResultVO("Empty"); }
        return successResultVO(imageService.r2Upload(imageFile));
    }
}
