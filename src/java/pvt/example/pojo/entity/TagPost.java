package pvt.example.pojo.entity;

/**
 * 信息：src/java/pvt/example/pojo/entity/TagPost.java
 * <p>日期：2025/9/11
 * <p>描述：
 */
public class TagPost {
    private Integer tagId;
    private Long postId;

    public Integer getTagId() { return tagId; }

    public void setTagId(Integer tagId) { this.tagId = tagId; }

    public Long getPostId() { return postId; }

    public void setPostId(Long postId) { this.postId = postId; }

    @Override
    public String toString() {
        return "TagPost{" +
                "tagId=" + tagId +
                ", postId=" + postId +
                '}';
    }
}
