package pvt.example.mapper;

import org.apache.ibatis.annotations.Mapper;
import pvt.example.pojo.dto.PostRequest;

/**
 * 信息：src/java/pvt/example/mapper/MiddleMapper.java
 * <p>日期：2025/8/20
 * <p>描述：中间表Mapper category_post tag_post
 */
@Mapper
public interface BridgeMapper {
    public Integer insertCategoryPost(PostRequest postCreate);

    public Integer insertTagPost(PostRequest postCreate);

    public Integer delCategoryPostByPostId(Long[] ids);

    public Integer delCategoryPostByCategoryId(Integer[] ids);

    public Integer delTagPostByPostId(Long[] ids);

    public Integer delTagPostByTagId(Integer[] ids);

    public Integer uploadCategoryPostByCategoryIdToZero(Integer[] ids);
}
