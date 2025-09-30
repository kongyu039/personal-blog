package pvt.example.service2.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pvt.example.common.utils.AppUtil;
import pvt.example.common.utils.FreemarkerUtil;
import pvt.example.mapper.BaseMapper;
import pvt.example.mapper.CategoryMapper;
import pvt.example.mapper.PostMapper;
import pvt.example.mapper.TagMapper;
import pvt.example.mapper2.BaseMapper2;
import pvt.example.pojo.entity.Category;
import pvt.example.pojo.entity.Post;
import pvt.example.pojo.entity.Tag;
import pvt.example.pojo.entity.TagPost;
import pvt.example.service2.BasicService2;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 信息：src/java/pvt/example/service2/impl/BasicService2Impl.java
 * <p>日期：2025/9/10
 * <p>描述：
 */
@Service
public class BasicService2Impl implements BasicService2 {
    /** 每批插入的数量（根据实际情况调整，建议500以内） */
    private static final int BATCH_SIZE = 300;
    @Resource
    private BaseMapper2 baseMapper2;
    @Resource
    private PostMapper postMapper;
    @Resource
    private CategoryMapper categoryMapper;
    @Resource
    private TagMapper tagMapper;
    @Resource
    private BaseMapper baseMapper;
    @Resource
    @Lazy
    private BasicService2 basicService2;
    private static final Logger logger = LoggerFactory.getLogger(BasicService2Impl.class);

    /** 初始化 Post 文章表 */
    private Integer initPostData() {
        Integer total = 0;
        List<List<Post>> postLists = AppUtil.splitList(postMapper.selectPostAllBasic(), BATCH_SIZE);
        for (List<Post> posts : postLists) { total += baseMapper2.batchInsertPost(posts); }
        return total;
    }

    /** 初始化 Category 分类表数据 */
    private Integer initCategoryData() {
        Integer total = 0;
        List<List<Category>> postLists = AppUtil.splitList(categoryMapper.selectCategories(), BATCH_SIZE);
        for (List<Category> category : postLists) { total += baseMapper2.batchInsertCategory(category); }
        return total;
    }

    /** 初始化 Tag 标签表数据 */
    private Integer initTagData() {
        Integer total = 0;
        List<List<Tag>> postLists = AppUtil.splitList(tagMapper.selectTags(), BATCH_SIZE);
        for (List<Tag> tagList : postLists) { total += baseMapper2.batchInsertTag(tagList); }
        return total;
    }

    /** 初始化 TagPost 中间表数据 */
    private Integer initTagPostData() {
        Integer total = 0;
        List<List<TagPost>> postLists = AppUtil.splitList(baseMapper.selectAllTagPost(), BATCH_SIZE);
        for (List<TagPost> tagPostList : postLists) { total += baseMapper2.batchInsertTagPost(tagPostList); }
        return total;
    }

    /** 初始化数据库文件结构 */
    private Integer genDataBase() {
        Integer total = 0;
        total += baseMapper2.genDataBase();
        total += baseMapper2.createPostTable();
        total += baseMapper2.createCategoryTable();
        total += baseMapper2.createTagTable();
        total += baseMapper2.createTagPostTable();
        return total;
    }

    /** 导入数据库文件信息 */
    private Integer initData2DataBase() {
        Integer total = 0;
        total += this.initPostData();
        total += this.initCategoryData();
        total += this.initTagData();
        total += this.initTagPostData();
        return total;
    }

    /** 创建生成目录 */
    @Override
    public Boolean createDir() {
        Path path = AppUtil.getJarDirectory("frontend");
        if (!Files.exists(path)) { return new File(path.toUri()).mkdirs(); }
        return true;
    }

    /** 清除生成目录 */
    @Override
    public Boolean cleanDir() { return AppUtil.delFileDir(AppUtil.getJarDirectory("frontend")); }

    /** 数据库和数据初始化 */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean handlerDataBase() {
        try {
            Files.deleteIfExists(AppUtil.getJarDirectory("frontend", "database.db"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        int total = 0;
        total += this.genDataBase();
        total += this.initData2DataBase();
        return total > 0;
    }

    /** 复制模版静态资源 */
    @Override
    public Boolean handlerCopyTmpls() {
        Path sourceDir = AppUtil.getJarDirectory("tmpl");
        Path targetDir = AppUtil.getJarDirectory("frontend");
        return AppUtil.copyDirWithSpringUtil(sourceDir, targetDir);
    }

    /** 渲染并生成模版 */
    @Override
    public Boolean handlerTmpls() {
        {/* 1. 生成index.html */}
        { // 2. 生成文章 post.html
            List<Post> posts = postMapper.selectPostAllBasic();
            for (Post post : posts) {
                Map<String, Object> dataModelPost = new HashMap<String, Object>();
                String dateFormatter = AppUtil.dateToFormatter(post.getCreatedAt());
                dataModelPost.put("post", post);
                File postFile = Paths.get(AppUtil.getJarDirectory(), "frontend", "posts", dateFormatter, post.getId() + ".html").toFile();
                FreemarkerUtil.generateFile("post.ftl", dataModelPost, postFile);
            }
        }
        return true;
    }

    /** 生成目录文件 */
    @Override
    public String genFileDir() {
        try { if (!basicService2.cleanDir()) { return "清除失败"; } } catch (Exception e) { return "清除失败"; }
        try { if (!basicService2.createDir()) { return "初始化目录失败"; } } catch (Exception e) { return "初始化目录失败"; }
        try { if (!basicService2.handlerCopyTmpls()) { return "Tmpls复制失败"; } } catch (Exception e) { return "Tmpls复制失败"; }
        try { if (!basicService2.handlerDataBase()) { return "数据库表初始化失败"; } } catch (Exception e) { return "数据库表初始化失败"; }
        try { if (!basicService2.handlerTmpls()) { return "模版填充失败"; } } catch (Exception e) { return "模版填充失败"; }
        return null;
    }

    @Override
    public byte[] zipFileDir() {
        byte[] zipBytes;
        // 提取到别的方法上;
        try {
            Path sourceDirPath = AppUtil.getJarDirectory("frontend"); // 源目录路径
            zipBytes = AppUtil.zipDirectory(sourceDirPath);// 调用工具类方法生成ZIP文件的字节数组
        } catch (IOException e) {
            logger.debug("zip打包失败");
            return null;
        }
        try {
            if (!basicService2.cleanDir()) {
                logger.debug("二次清除失败");
                return null;
            }
        } catch (Exception e) {
            logger.debug("二次清除失败");
            return null;
        }
        return zipBytes;
    }
}
