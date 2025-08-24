package pvt.example.pojo.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.validation.constraints.Size;
import java.util.Arrays;
import java.util.Date;

/**
 * 信息：src/java/pvt/example/pojo/dto/PostRequest.java
 * <p>日期：2025/8/18
 * <p>描述：文章Post的请求数据传输对象(DTO)
 */
public class PostRequest {
    private Long id;
    private String cover;
    @Size(min = 1,max = 30, message = "字符串长度不能超过30个字符")
    private String title;
    @Size(max = 30, message = "字符串长度不能超过30个字符")
    private String summary;
    private String content;
    private String htmlContent;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Shanghai")
    private Date createdAt;         // 创建时间
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Shanghai")
    private Date updatedAt;         // 更新时间
    private Integer categoryId;
    private Integer[] tagIds;

    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

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

    public Integer getCategoryId() { return categoryId; }

    public void setCategoryId(Integer categoryId) { this.categoryId = categoryId; }

    public Integer[] getTagIds() { return tagIds; }

    public void setTagIds(Integer[] tagIds) { this.tagIds = tagIds; }

    @Override
    public String toString() {
        return "PostRequest{" +
                "id=" + id +
                ", cover='" + cover + '\'' +
                ", title='" + title + '\'' +
                ", summary='" + summary + '\'' +
                ", content='" + content + '\'' +
                ", htmlContent='" + htmlContent + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", categoryId=" + categoryId +
                ", tagIds=" + Arrays.toString(tagIds) +
                '}';
    }
}
