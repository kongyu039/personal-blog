package pvt.example.pojo.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;

import javax.validation.constraints.Size;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 信息：src/java/pvt/example/entity/Post.java
 * <p>日期：2025/8/1
 * <p>描述：文章实体类 [文章N-标签N] [文章1-分类N]
 */
public class Post {
    private Integer id;             // 主键(用雪花算法生成)
    private String cover;           // 封面URL
    private String title;           // 标题
    @Size(max = 30, message = "字符串长度不能超过30个字符")
    private String summary;         // 文章概括/文章摘要
    private String content;         // 内容
    private String htmlContent;     // HTML渲染内容
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Shanghai")
    private Date createdAt;         // 创建时间
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Shanghai")
    private Date updatedAt;         // 更新时间
    private Integer isTop;          // 是否置顶
    private Integer isDel;          // 是否删除
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Category category;      // 一对一关系
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<Tag> tags;         // 一对多关系

    public Integer getId() { return id; }

    public void setId(Integer id) { this.id = id; }

    public String getCover() { return cover; }

    public void setCover(String cover) { this.cover = cover; }

    public String getTitle() { return title; }

    public void setTitle(String title) { this.title = title; }

    public String getSummary() { return summary; }

    public void setSummary(String summary) { this.summary = summary; }

    public String getContent() { return content; }

    public void setContent(String content) { this.content = content; }

    public String getHtmlContent() { return htmlContent; }

    public void setHtmlContent(String htmlContent) { this.htmlContent = htmlContent; }

    public Date getCreatedAt() { return createdAt; }

    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }

    public Date getUpdatedAt() { return updatedAt; }

    public void setUpdatedAt(Date updatedAt) { this.updatedAt = updatedAt; }

    public Integer getIsTop() { return isTop; }

    public void setIsTop(Integer isTop) { this.isTop = isTop; }

    public Integer getIsDel() { return isDel; }

    public void setIsDel(Integer isDel) { this.isDel = isDel; }

    public Category getCategory() { return category; }

    public void setCategory(Category category) { this.category = category; }

    public List<Tag> getTags() { return tags; }

    public void setTags(List<Tag> tags) { this.tags = tags; }

    @Override
    public String toString() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        return "Post{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", htmlContent='" + htmlContent + '\'' +
                ", createdAt=" + (createdAt != null ? dateFormat.format(createdAt) : "null") +
                ", updatedAt=" + (updatedAt != null ? dateFormat.format(updatedAt) : "null") +
                ", tags=" + tags +
                ", category=" + category +
                '}';
    }
}
