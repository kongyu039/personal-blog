package pvt.example.pojo.vo;

import java.util.List;

/**
 * 信息：src/java/pvt/example/pojo/vo/ResultPageVO.java
 * <p>日期：2025/8/9
 * <p>描述：分页结果的封装
 */
public class ResultPageVO<T> {
    private List<T> results; // 当前页的数据
    private Integer totalCount; // 总记录数
    private Integer pageNumber; // 当前页码
    private Integer pageSize; // 每页大小

    public ResultPageVO() { }

    public ResultPageVO(List<T> results, Integer totalCount, Integer pageNumber, Integer pageSize) {
        this.results = results;
        this.totalCount = totalCount;
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
    }

    public List<T> getResults() { return results; }

    public void setResults(List<T> results) { this.results = results; }

    public Integer getTotalCount() { return totalCount; }

    public void setTotalCount(Integer totalCount) { this.totalCount = totalCount; }

    public Integer getPageNumber() { return pageNumber; }

    public void setPageNumber(Integer pageNumber) { this.pageNumber = pageNumber; }

    public Integer getPageSize() { return pageSize; }

    public void setPageSize(Integer pageSize) { this.pageSize = pageSize; }
}