package pvt.example.pojo.entity;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

/**
 * 信息：src/java/pvt/example/entity/Category.java
 * <p>日期：2025/8/1
 * <p>描述：分类实体类
 */
public class Category {
    private Integer id; // 标签id
    private String name; // 标签名称
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String summary; // 分类摘要
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<Post> posts; // 一对多关系

    public Integer getId() { return id; }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() { return name; }

    public void setName(String name) {
        this.name = name;
    }

    public String getSummary() { return summary; }

    public void setSummary(String summary) { this.summary = summary; }

    public List<Post> getPosts() { return posts; }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
    }

    @Override
    public String toString() {
        return "Category{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", posts=" + posts +
                '}';
    }
}
