package pvt.example.pojo.entity;

/**
 * 信息：src/java/pvt/example/pojo/entity/ImageStorage.java
 * <p>日期：2025/8/26
 * <p>描述：本地图片存储 实体类
 */
public class ImageStorage {
    private Integer id; // 主键
    private String key; // 存储地址唯一Key
    private Integer flag; // 是否存储 本地/远程

    public Integer getId() { return id; }

    public void setId(Integer id) { this.id = id; }

    public String getKey() { return key; }

    public void setKey(String key) { this.key = key; }

    public Integer getFlag() { return flag; }

    public void setFlag(Integer flag) { this.flag = flag; }

    @Override
    public String toString() {
        return "ImageStorage{" +
                "id=" + id +
                ", key='" + key + '\'' +
                ", flag=" + flag +
                '}';
    }
}
