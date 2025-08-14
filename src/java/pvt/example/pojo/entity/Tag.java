package pvt.example.pojo.entity;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

/**
 * 信息：src/java/pvt/example/entity/Tag.java
 * <p>日期：2025/8/1
 * <p>描述：标签实体类 [标签N 对 文章N]
 */
public class Tag {
    private Integer id; // 标签id
    private String name; // 标签名称(Name就是Summary摘要,不要太复杂)
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

    public List<Post> getPosts() { return posts; }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
    }

    @Override
    public String toString() {
        return "Tag{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", posts=" + posts +
                '}';
    }
}
