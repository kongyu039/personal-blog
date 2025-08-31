package pvt.example.pojo.query;

import java.util.List;

/**
 * 信息：src/java/pvt/example/pojo/query/PostQuery.java
 * <p>日期：2025/8/9
 * <p>描述：PostQuery 搜索实体类
 */
public class PostQuery {
    private String title;
    private String titleQuery;
    private String summary;
    private String summaryQuery;
    private Long categoryId;
    private List<Long> tagIds;
    private Integer page;
    private Integer limit;

    public String getTitleQuery() { return titleQuery; }

    public void setTitleQuery(String titleQuery) { this.titleQuery = titleQuery; }

    public String getSummaryQuery() { return summaryQuery; }

    public void setSummaryQuery(String summaryQuery) { this.summaryQuery = summaryQuery; }

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
                ", titleQuery='" + titleQuery + '\'' +
                ", summary='" + summary + '\'' +
                ", summaryQuery='" + summaryQuery + '\'' +
                ", categoryId=" + categoryId +
                ", tagIds=" + tagIds +
                ", page=" + page +
                ", limit=" + limit +
                '}';
    }
}
