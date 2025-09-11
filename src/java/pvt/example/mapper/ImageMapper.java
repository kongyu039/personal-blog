package pvt.example.mapper;

import org.apache.ibatis.annotations.Mapper;
import pvt.example.pojo.entity.ImageStorage;

import java.util.List;

/**
 * 信息：src/java/pvt/example/mapper/ImageMapper.java
 * <p>日期：2025/9/1
 * <p>描述：
 */
@Mapper
public interface ImageMapper {
    public Integer insertImageStorage(ImageStorage imageStorage);

    public List<ImageStorage> selectImageByFlag(Integer flag);

    public ImageStorage selectImageById(Integer id);

    public Integer deleteImageById(Integer id);
}
