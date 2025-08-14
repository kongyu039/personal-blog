package pvt.example.pojo.entity;

/**
 * 信息：src/java/pvt/example/entity/Setting.java
 * <p>日期：2025/8/8
 * <p>描述：设置实体类
 */
public class Setting {
    private String key; // 键
    private String value; // 值
    private String comment; // 注释

    public String getKey() { return key; }

    public void setKey(String key) { this.key = key; }

    public String getValue() { return value; }

    public void setValue(String value) { this.value = value; }

    public String getComment() { return comment; }

    public void setComment(String comment) { this.comment = comment; }

    @Override
    public String toString() {
        return "Setting{" +
                "key='" + key + '\'' +
                ", value='" + value + '\'' +
                ", comment='" + comment + '\'' +
                '}';
    }
}
