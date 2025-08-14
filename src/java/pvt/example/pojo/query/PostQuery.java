package pvt.example.pojo.query;

import java.util.List;

/**
 * 信息：src/java/pvt/example/pojo/query/PostQuery.java
 * <p>日期：2025/8/9
 * <p>描述：PostQuery 搜索实体类
 */
public class PostQuery {
    private String title;
    private String summary;
    private Long categoryId;
    private List<Long> tagIds;
    private Integer page;
    private Integer limit;

    public String getTitle() { return title; }

    public void setTitle(String title) { this.title = title; }

    public String getSummary() { return summary; }

    public void setSummary(String summary) { this.summary = summary; }

    public Long getCategoryId() { return categoryId; }

    public void setCategoryId(Long categoryId) { this.categoryId = categoryId; }

    public List<Long> getTagIds() { return tagIds; }

    public void setTagIds(List<Long> tagIds) { this.tagIds = tagIds; }

    public Integer getPage() { return page; }

    public void setPage(Integer page) { this.page = page; }

    public Integer getLimit() { return limit; }

    public void setLimit(Integer limit) { this.limit = limit; }

    @Override
    public String toString() {
        return "PostQuery{" +
                "title='" + title + '\'' +
                ", summary='" + summary + '\'' +
                ", categoryId=" + categoryId +
                ", tagIds=" + tagIds +
                ", page=" + page +
                ", limit=" + limit +
                '}';
    }
}
